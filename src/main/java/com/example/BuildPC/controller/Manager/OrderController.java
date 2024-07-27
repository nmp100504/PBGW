package com.example.BuildPC.controller.Manager;

import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.model.*;
import com.example.BuildPC.service.OrderDetailService;
import com.example.BuildPC.service.OrderService;
import com.example.BuildPC.dto.OrderDTO;
import com.example.BuildPC.service.ShoppingCartService;
import com.example.BuildPC.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller

public class OrderController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    ShoppingCartService shoppingCartService;



    @GetMapping("/ManagerDashBoard/orderList")
    public String showManagerDashBoard(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, Model model) {
        if (date == null) {
            date = new Date();
            model.addAttribute("OrderList", orderService.listAllOrder());
            model.addAttribute("countWAIT", orderService.countOrderByStatus(Status.WAIT));
            System.out.println("----------------------------------------------------------");
            System.out.println(orderService.countOrderByStatus(Status.WAIT));
            System.out.println("----------------------------------------------------------");
            model.addAttribute("countINPROGRESS", orderService.countOrderByStatus(Status.IN_PROGRESS));
            System.out.println("----------------------------------------------------------");
            System.out.println(orderService.countOrderByStatus(Status.IN_PROGRESS));
            System.out.println("----------------------------------------------------------");
            model.addAttribute("countDONE", orderService.countOrderByStatus(Status.DONE));
            model.addAttribute("countCANCEL", orderService.countOrderByStatus(Status.CANCEL));
        } else {
            model.addAttribute("OrderList", orderService.listByOrdersDate(date));

            model.addAttribute("countWAIT", orderService.countOrdersByStatusAndDate(Status.WAIT, date));
            model.addAttribute("countINPROGRESS", orderService.countOrdersByStatusAndDate(Status.IN_PROGRESS, date));
            model.addAttribute("countDONE", orderService.countOrdersByStatusAndDate(Status.DONE, date));
            model.addAttribute("countCANCEL", orderService.countOrdersByStatusAndDate(Status.CANCEL, date));
            model.addAttribute("selectedDate", date);
        }
        model.addAttribute("monthlyTotal", orderService.getTotalOrderValueByMonth());
        model.addAttribute("monthlyOrderCount", orderService.getOrderCountByMonth());
        return "Manager/showOrderList";
    }

    @GetMapping("/orderList/{code}")
    public String showOrderListByStatus(@PathVariable String code, @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, Model model) {
        System.out.println("----------------------------------------------------");
        System.out.println("Ngay" + date);
        System.out.println(code);
        System.out.println("----------------------------------------------------");
        if (date == null) {
            date = new Date();
            model.addAttribute("countWAIT", orderService.countOrderByStatus(Status.WAIT));
            model.addAttribute("countINPROGRESS", orderService.countOrderByStatus(Status.IN_PROGRESS));
            model.addAttribute("countDONE", orderService.countOrderByStatus(Status.DONE));
            model.addAttribute("countCANCEL", orderService.countOrderByStatus(Status.CANCEL));
            if (code.equals("INP")) {
                model.addAttribute("OrderList", orderService.listByOrderStatus(Status.IN_PROGRESS));
            }
            if (code.equals("DONE")) {
                model.addAttribute("OrderList", orderService.listByOrderStatus(Status.DONE));
            }
            if (code.equals("CAN")) {
                System.out.println("----------------------------------------------------");
                System.out.println("in ra CAN");
                System.out.println("----------------------------------------------------");
                model.addAttribute("OrderList", orderService.listByOrderStatus(Status.CANCEL));
            }
            if (code.equals("WAIT")) {
                System.out.println("----------------------------------------------------");
                System.out.println("in ra WAIT");
                System.out.println("----------------------------------------------------");
                model.addAttribute("OrderList", orderService.listByOrderStatus(Status.WAIT));
            }
        } else {
            if (code.equals("INP")) {
                model.addAttribute("OrderList", orderService.listOrdersByStatusAndDate(Status.IN_PROGRESS, date));
            }
            if (code.equals("DONE")) {
                model.addAttribute("OrderList", orderService.listOrdersByStatusAndDate(Status.DONE, date));
            }
            if (code.equals("CAN")) {
                model.addAttribute("OrderList", orderService.listOrdersByStatusAndDate(Status.CANCEL, date));
            }
            if (code.equals("WAIT")) {
                model.addAttribute("OrderList", orderService.listOrdersByStatusAndDate(Status.WAIT, date));
            }
            model.addAttribute("countWAIT", orderService.countOrdersByStatusAndDate(Status.WAIT, date));
            model.addAttribute("countINPROGRESS", orderService.countOrdersByStatusAndDate(Status.IN_PROGRESS, date));
            model.addAttribute("countDONE", orderService.countOrdersByStatusAndDate(Status.DONE, date));
            model.addAttribute("countCANCEL", orderService.countOrdersByStatusAndDate(Status.CANCEL, date));
            model.addAttribute("selectedDate", date);
        }
        model.addAttribute("monthlyTotal", orderService.getTotalOrderValueByMonth());
        model.addAttribute("monthlyOrderCount", orderService.getOrderCountByMonth());
        return "Manager/showOrderList";

    }

    public String showOrders(Model model) {
        List<OrderDTO> orderList = orderService.listAllOrder();
        model.addAttribute("orderList", orderList);
        return "ManagerDashBoard";
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<User> currentUser = userService.findByEmail(userDetails.getEmail());
        if (currentUser.isPresent()) {
            List<CartItem> cartItems = shoppingCartService.listCartItems(currentUser.get());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("cartItems", cartItems);
            BigDecimal orderTotal = cartItems.stream()
                    .map(item -> BigDecimal.valueOf(item.getProduct().getProductSalePrice())
                            .multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // Modify if there are additional charges like shipping
            model.addAttribute("orderTotal", orderTotal);
            return "LandingPage/checkout";
        }

        return "auth/login_page";
    }

    @PostMapping("/saveBilling")
    public String saveAddress(
            @RequestParam String streetaddress,
            @RequestParam(required = false) String apartmentaddress,
            @RequestParam String town,
            @RequestParam String country,
            @RequestParam String postcode,
            @RequestParam String email,
            @RequestParam String telephone,
            @RequestParam(required = false) String note,
            Model model) {

        // Combine the address fields into a single string
        StringBuilder fullAddress = new StringBuilder();
        fullAddress.append(streetaddress);
        if (apartmentaddress != null && !apartmentaddress.isEmpty()) {
            fullAddress.append(", ").append(apartmentaddress);
        }
        fullAddress.append(", ").append(town)
                .append(", ").append(country)
                .append(", ").append(postcode);
        Optional<User> currentUser = userService.getCurrentUser();
//        if (currentUser.isPresent()) {
//            User user = currentUser.get();
//            List<CartItem> cartItems = shoppingCartService.listCartItems(user);
//
//            model.addAttribute("cartItems", cartItems);
//            return "LandingPage/checkout";
//        } else {
//            // Handle case where user is not found (though ideally, should not happen)
//            return "redirect:/login"; // Redirect to login page or handle appropriately
//        }

        // Save the full address to the database
        // Assuming you have a User entity with an address field
        LocalDate currentDate = LocalDate.now();
        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<CartItem> itemsList = shoppingCartService.listCartItems(currentUser.get());


        Order order = new Order((java.sql.Date) date, note, fullAddress.toString(), currentUser.get());
        orderService.saveOrder(order);
        for (CartItem item : itemsList) {
            OrderDetail od = new OrderDetail(item.getQuantity(), (float) 0, order, item.getProduct());
        }
//        currentUser.setAddress(fullAddress.toString());


        // Add success message to the model
        model.addAttribute("message", "Billing saved successfully!");

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") int id) {
        orderDetailService.deleteByOrder(orderService.getOrderById(id));
        orderService.deleteOrderById(id);
        return "redirect:/ManagerDashBoard";
    }

    @GetMapping("/detail/{id}")
    public String showOrderDetail(@PathVariable("id") int id, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderDetail> orderDetails = orderDetailService.findByOrder(order);
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        return "Manager/orderdetail";
    }

    @PostMapping("order/save")
    public String saveOrder(@ModelAttribute("order") Order order) {
        orderService.saveOrder(order);
        return "redirect:/detail/" + order.getId();  // Redirect to the order detail page after saving
    }


}
