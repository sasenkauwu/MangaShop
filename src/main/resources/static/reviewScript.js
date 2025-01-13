document.addEventListener('DOMContentLoaded', function () {
    const productId = window.location.pathname.split("/")[2];
    const reviewsList = document.getElementById('reviews-list');
    const addReviewForm = document.getElementById('add-review-form');

    let currentUserEmail = null;

    function fetchCurrentUser() {
        return fetch('/api/user/me')
            .then(response => response.ok ? response.json() : null)
            .then(user => {
                if (user) {
                    currentUserEmail = user.email;
                } else {
                    console.warn('User is not authenticated.');
                }
            })
            .catch(error => console.error('Failed to load current user:', error));
    }

    function loadReviews() {
        fetch(`/api/review/getAll/${productId}`)
            .then(response => response.json())
            .then(reviews => {
                reviewsList.innerHTML = '';
                reviews.forEach(review => {
                    const reviewCard = document.createElement('div');
                    reviewCard.classList.add('card', 'mb-3', 'shadow-sm');

                    const isAuthor = currentUserEmail === review.userEmail;

                    reviewCard.innerHTML = `
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <h5 class="card-title mb-0">${review.userEmail}</h5>
                                <small class="text-muted">${new Date(review.reviewDate).toLocaleDateString()}</small>
                            </div>
                            <div class="mt-2">
                                <strong>Rating:</strong>
                                <span class="review-rating">${'⭐'.repeat(review.rating)}</span>
                                ${isAuthor ? `<input type="number" class="form-control d-none edit-rating" min="1" max="5" value="${review.rating}">` : ''}
                            </div>
                            <p class="card-text mt-2">${review.comment}</p>
                            ${isAuthor ? `<textarea class="form-control d-none edit-comment">${review.comment}</textarea>` : ''}
                            ${isAuthor ? `
                                <div class="d-flex justify-content-end mt-3">
                                    <button class="btn btn-outline-primary btn-sm edit-btn">Edit</button>
                                    <button class="btn btn-outline-success btn-sm save-btn d-none" data-review-id="${review.reviewId}">Save</button>
                                    <button class="btn btn-outline-secondary btn-sm cancel-btn d-none">Cancel</button>
                                    <button class="btn btn-outline-danger btn-sm delete-review" data-review-id="${review.reviewId}">Delete</button>
                                </div>
                            ` : ''}
                        </div>
                    `;
                    reviewsList.appendChild(reviewCard);
                });

                document.querySelectorAll('.edit-btn').forEach(button => {
                    button.addEventListener('click', handleEditMode);
                });
                document.querySelectorAll('.save-btn').forEach(button => {
                    button.addEventListener('click', handleSaveReview);
                });
                document.querySelectorAll('.cancel-btn').forEach(button => {
                    button.addEventListener('click', handleCancelEdit);
                });
                document.querySelectorAll('.delete-review').forEach(button => {
                    button.addEventListener('click', handleDeleteReview);
                });
            })
            .catch(error => {
                console.error('Error while loading reviews:', error);
                reviewsList.innerHTML = '<p class="text-danger">Failed to load reviews.</p>';
            });
    }

    function handleEditMode(event) {
        const card = event.currentTarget.closest('.card');
        card.querySelector('.review-rating').classList.add('d-none');
        card.querySelector('.edit-rating').classList.remove('d-none');
        card.querySelector('.card-text').classList.add('d-none');
        card.querySelector('.edit-comment').classList.remove('d-none');

        card.querySelector('.edit-btn').classList.add('d-none');
        card.querySelector('.save-btn').classList.remove('d-none');
        card.querySelector('.cancel-btn').classList.remove('d-none');
    }

    function handleCancelEdit(event) {
        const card = event.currentTarget.closest('.card');
        card.querySelector('.review-rating').classList.remove('d-none');
        card.querySelector('.edit-rating').classList.add('d-none');
        card.querySelector('.card-text').classList.remove('d-none');
        card.querySelector('.edit-comment').classList.add('d-none');

        card.querySelector('.edit-btn').classList.remove('d-none');
        card.querySelector('.save-btn').classList.add('d-none');
        card.querySelector('.cancel-btn').classList.add('d-none');
    }

    function handleSaveReview(event) {
        const card = event.currentTarget.closest('.card');
        const reviewId = event.currentTarget.getAttribute('data-review-id');
        const newRating = card.querySelector('.edit-rating').value;
        const newComment = card.querySelector('.edit-comment').value;

        fetch(`/api/review/update/${reviewId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                rating: parseInt(newRating, 10),
                comment: newComment,
            }),
        })
            .then(response => {
                if (response.ok) {
                    loadReviews();
                } else {
                    alert('Failed to update review.');
                }
            })
            .catch(error => console.error('Error while updating review:', error));
    }

    function handleDeleteReview(event) {
        const reviewId = event.currentTarget.getAttribute('data-review-id');

            fetch(`/api/review/delete/${reviewId}`, {
                method: 'DELETE',
            })
                .then(response => {
                    if (response.ok) {
                        loadReviews();
                    } else {
                        alert('Failed to delete review.');
                    }
                })
                .catch(error => console.error('Error while deleting review:', error));

    }

    if (addReviewForm) {
        addReviewForm.addEventListener('submit', function (event) {
            event.preventDefault();

            const rating = document.getElementById('rating').value;
            const comment = document.getElementById('comment').value;

            if (rating && comment) {
                fetch(`/api/review/save`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        productId: productId,
                        rating: parseInt(rating, 10),
                        comment: comment,
                    }),
                })
                    .then(response => {
                        if (response.ok) {
                            loadReviews();
                            addReviewForm.reset();
                        } else {
                            alert('Failed to add review.');
                        }
                    })
                    .catch(error => console.error('Error while adding review:', error));
            }
        });
    }

    fetchCurrentUser().then(loadReviews);
});
