package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.mapper.CommentMapper;
import com.example.BuildPC.mapper.PostMapper;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.User;
import com.example.BuildPC.model.Vote;
import com.example.BuildPC.repository.PostRepository;
import com.example.BuildPC.repository.UserRepository;
import com.example.BuildPC.repository.VoteRepository;
import com.example.BuildPC.service.PostService;
import com.example.BuildPC.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
//    private final Path storageLocation = Paths.get("public/images/thumbnail/"); // Set your upload directory

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public List<PostDto> findAllPost() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostMapper::mapToPostDTO).toList();
    }

    @Override
    public void createPost(PostDto postDto, MultipartFile thumbnail) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> user = userRepository.findByEmail(email);

        Post post = PostMapper.mapToPost(postDto);
        if (thumbnail != null && !thumbnail.isEmpty()) {
            try {
                post.setThumbnailData(thumbnail.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save thumbnail image", e);
            }
        }

        if (user.isPresent()) {
            post.setCreatedBy(user.get());
            postRepository.save(post);
        }
    }

    @Override
    public PostDto findPostById(long id) {
        Post post = postRepository.findById(id).get();
        return PostMapper.mapToPostDTO(post);
    }

    @Override
    public void updatePost(PostDto postDto, MultipartFile thumbnail) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> user = userRepository.findByEmail(email);

        Post existingPost = postRepository.findById(postDto.getId()).orElseThrow(() -> new RuntimeException("Post not found"));

        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        existingPost.setUrl(postDto.getUrl());
        existingPost.setShortDescription(postDto.getShortDescription());
        existingPost.setCreatedOn(postDto.getCreatedOn());
        existingPost.setUpdatedOn(postDto.getUpdatedOn());
        existingPost.setComments(postDto.getComments().stream()
                .map(CommentMapper::mapToComment)
                .collect(Collectors.toSet()));

        if (thumbnail != null && !thumbnail.isEmpty()) {
            try {
                existingPost.setThumbnailData(thumbnail.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save thumbnail image", e);
            }
        }

        if (user.isPresent()) {
            existingPost.setCreatedBy(user.get());
            postRepository.save(existingPost);
        }
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDto findPostByUrl(String postUrl) {
        Post post = postRepository.findByUrl(postUrl).get();
        return PostMapper.mapToPostDTO(post);
    }

//    @Override
//    public List<PostDto> searchPosts(String query) {
//        List<Post> posts = postRepository.searchPosts(query);
//        return posts.stream().map(PostMapper::mapToPostDTO).collect(Collectors.toList());
//    }

    @Override
    public Page<PostDto> findPaginatedPost(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo -1, pageSize);
        return this.postRepository.findAll(pageable).map(PostMapper::mapToPostDTO);
    }

    @Override
    public List<PostDto> findSortedPost(String field) {
        List<Post> sortedPosts = postRepository.findAll(Sort.by(Sort.Direction.ASC, field));
        return sortedPosts.stream()
                .map(PostMapper::mapToPostDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Page<PostDto> findSortedPaginatedPost(String field, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, field));
        return postRepository.findAll(pageable).map(PostMapper::mapToPostDTO);
    }

    @Override
    public Page<PostDto> findSortedPaginatedPostByAuthor(String field, Long authorId, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, field));
        return postRepository.findAllByCreatedById(authorId, pageable).map(PostMapper::mapToPostDTO);
    }

    @Override
    public Page<PostDto> findThreeMostRecentPostsByAuthor(Long authorId) {
        return findSortedPaginatedPostByAuthor("createdOn", authorId, 3);
    }

    @Override
    public List<PostDto> searchPosts(String query) {
        List<Post> posts = postRepository.searchPosts(query);
        return posts.stream().map(PostMapper::mapToPostDTO).collect(Collectors.toList());
    }


    @Override
    public Map<String, Long> getPostsCountByDayInCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY).plusDays(1);

        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(LocalTime.MAX);

        List<Object[]> results = postRepository.countPostsByDayInCurrentWeek(startDateTime, endDateTime);

        // Initialize the map with all dates of the current week set to 0
        Map<LocalDate, Long> postsCountByDay = IntStream.range(0, 7)
                .mapToObj(startOfWeek::plusDays)
                .collect(Collectors.toMap(
                        date -> date,
                        date -> 0L,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // Update the map with actual counts from the query results
        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate(); // Convert java.sql.Date to LocalDate
            Long count = (Long) result[1];
            postsCountByDay.put(date, count);
        }

        // Convert the map to a LinkedHashMap to maintain order
        Map<String, Long> sortedPostsCountByDay = postsCountByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return sortedPostsCountByDay;
    }

    @Override
    public Map<String, Long> getPostsCountByWeekInCurrentMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.atTime(LocalTime.MAX);

        List<Object[]> results = postRepository.countPostsByWeekInCurrentMonth(startDateTime, endDateTime);

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        Map<String, Long> postsCountByWeek = new HashMap<>();

        // Initialize the map with all weeks of the current month set to 0
        LocalDate current = startOfMonth;
        int weekNumber = 1;
        while (current.isBefore(endOfMonth) || current.isEqual(endOfMonth)) {
            String weekLabel = "Week " + weekNumber + " Month " + current.getMonthValue();
            postsCountByWeek.put(weekLabel, 0L);
            current = current.plusWeeks(1);
            weekNumber++;
        }

        // Update the map with actual counts from the query results
        for (Object[] result : results) {
            int year = (int) result[0];
            int month = (int) result[1];
            int weekOfYear = (int) result[2];
            Long count = (Long) result[3];

            if (year == today.getYear() && month == today.getMonthValue()) {
                LocalDate weekStart = LocalDate.ofYearDay(year, (weekOfYear - 1) * 7 + 1).with(weekFields.dayOfWeek(), 1);
                int weekOfMonth = weekStart.withDayOfMonth(1).plusDays(weekOfYear - 1).get(weekFields.weekOfMonth());

                String weekLabel = "Week " + weekOfMonth + " Month " + month;
                postsCountByWeek.merge(weekLabel, count, Long::sum);
            }
        }


        // Sort entries by week number
        return postsCountByWeek.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparingInt(key -> Integer.parseInt(key.split(" ")[1]))))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));    }


    public Map<String, Long> getPostsCountByMonthInCurrentYear() {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);
        LocalDate endOfYear = today.withDayOfYear(today.lengthOfYear());

        LocalDateTime startDateTime = startOfYear.atStartOfDay();
        LocalDateTime endDateTime = endOfYear.atTime(LocalTime.MAX);

        List<Object[]> results = postRepository.countPostsByMonthInCurrentYear(startDateTime, endDateTime);

        // Initialize the map with all months of the current year set to 0
        Map<String, Long> postsCountByMonth = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) {
            String monthLabel = LocalDate.of(today.getYear(), month, 1).getMonth().name() + " " + today.getYear();
            postsCountByMonth.put(monthLabel, 0L);
        }

        // Update the map with actual counts from the query results
        for (Object[] result : results) {
            int year = (int) result[0];
            int month = (int) result[1];
            Long count = (Long) result[2];

            if (year == today.getYear()) {
                String monthLabel = LocalDate.of(year, month, 1).getMonth().name() + " " + year;
                postsCountByMonth.put(monthLabel, count);
            }
        }

        return postsCountByMonth;
    }

    public Map<String, Long> getTopUsersByPostCount() {
        List<Object[]> results = postRepository.findTopUsersByPostCount();

        // Initialize the map to hold user names and post counts
        Map<String, Long> topUsersByPostCount = new LinkedHashMap<>();

        // Process the results and populate the map
        for (int i = 0; i < Math.min(10, results.size()); i++) {
            Object[] result = results.get(i);
            User user = (User) result[0];
            Long postCount = (Long) result[1];
            topUsersByPostCount.put(user.getEmail(), postCount); // Assuming User entity has getUsername() method
        }

        return topUsersByPostCount;
    }

    public Map<String, Long> getPostsCountByUser() {
        List<Object[]> results = postRepository.countPostsByUser();

        // Initialize the map to hold usernames and post counts
        Map<String, Long> postsCountByUser = new LinkedHashMap<>();

        // Process the results and populate the map
        for (Object[] result : results) {
            User user = (User) result[0];
            Long postCount = (Long) result[1];
            postsCountByUser.put(user.getEmail(), postCount); // Assuming User entity has getUsername() method
        }

        return postsCountByUser;
    }
}
