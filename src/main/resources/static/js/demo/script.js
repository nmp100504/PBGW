document.addEventListener('DOMContentLoaded', (event) => {
    $('#orderModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var orderId = button.data('order-id'); // Extract info from data-* attributes

        // Fetch order details using orderId (this part needs to be implemented as per your backend logic)
        // For demonstration purposes, I'm using static data
        // var orderDetails = {
        //     id: orderId,
        //     status: "Shipped",
        //     billingDetails: "John Doe, 123 Main St, Springfield",
        //     paymentMethod: "Credit Card",
        //     shippingDetails: "UPS, Tracking #123456789",
        //     itemsOrdered: "Item1, Item2, Item3"
        // };
        //
        // var modal = $(this);
        // modal.find('#modalOrderId').text(orderDetails.id);
        // modal.find('#modalStatus').text(orderDetails.status);
        // modal.find('#modalBillingDetails').text(orderDetails.billingDetails);
        // modal.find('#modalPaymentMethod').text(orderDetails.paymentMethod);
        // modal.find('#modalShippingDetails').text(orderDetails.shippingDetails);
        // modal.find('#modalItemsOrdered').text(orderDetails.itemsOrdered);
    });
});