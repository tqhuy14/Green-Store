let userIdToDelete = null;

function showConfirmModal(userId) {
    userIdToDelete = userId;
    document.getElementById("confirmModal").style.display = "flex";
}

function closeModal() {
    document.getElementById("confirmModal").style.display = "none";
}

function deleteUser() {
    if (userIdToDelete !== null) {
        window.location.href = '/users/' + userIdToDelete + '/delete';
    }
}
