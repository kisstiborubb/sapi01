$(function() {

    $(".well").on("click", "#delete-group-link", function(e) {
        e.preventDefault();

        var groupDeleteDialogTempate = Handlebars.compile($("#template-delete-group-confirmation-dialog").html());

        $("#view-holder").append(groupDeleteDialogTempate());
        $("#delete-group-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-group-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-group-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-group-button", function(e) {
        e.preventDefault();
        window.location.href = "/group/delete/" + $("#group-id").text();
    });
});
