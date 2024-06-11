$(document).ready(function(){
    $(".input_number_decrement").on("click", function (evt) {
        evt.preventDefault();
        var productId = $(this).attr("pid"); // Get the product id from the button
        var qtyInput = $("#quantity" + productId);

        var newQty = parseInt(qtyInput.val()) - 1;
        if(newQty > 0) qtyInput.val(newQty);
    });

    $(".input_number_increment").on("click", function (evt) {
        evt.preventDefault();
        var productId = $(this).attr("pid"); // Get the product id from the button
        var qtyInput = $("#quantity" + productId);

        var newQty = parseInt(qtyInput.val()) + 1;
        qtyInput.val(newQty);
    });
});



