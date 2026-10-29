// PUT 요청 (수정)
const editForms = document.querySelectorAll('.edit-form');
editForms.forEach(form => {
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        fetch(this.action, {
            method: 'PUT',
            body: new FormData(this)
        }).then(() => window.location.href = this.action.replace('/edit', ''));
    });
});

// DELETE 요청 (삭제)
const deleteBtns = document.querySelectorAll('.delete-btn');
deleteBtns.forEach(btn => {
    btn.addEventListener('click', function() {
        fetch(this.dataset.url, { method: 'DELETE' })
            .then(() => window.location.href = '/articles');
    });
});