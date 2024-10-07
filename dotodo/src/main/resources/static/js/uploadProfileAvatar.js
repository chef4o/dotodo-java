document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('profilePicture');
    const fileChosen = document.getElementById('file-chosen');

    fileInput.addEventListener('change', function () {
        if (this.files && this.files[0]) {
            fileChosen.textContent = this.files[0].name;
        } else {
            fileChosen.textContent = 'No file selected';
        }
    });
});