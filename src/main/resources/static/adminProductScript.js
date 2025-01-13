document.getElementById('submitBtn').addEventListener('click', async function () {
    const form = document.getElementById('productForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/api/product/save', {
            method: 'POST',
            body: formData,
        });

        if (response.ok) {
            window.location.href = '/products';
        } else {
            const error = await response.text();
            alert('Error: ' + error);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An unexpected error occurred.');
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.delete-product-btn');

    deleteButtons.forEach(button => {
        button.addEventListener('click', async function () {
            const productId = this.dataset.productId;

            if (confirm('Are you sure you want to delete this product?')) {
                try {
                    const response = await fetch(`/api/product/delete/${productId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        this.closest('.col').remove();
                        window.location.href = '/products';
                    } else {
                        const error = await response.text();
                        alert(`Error: ${error}`);
                    }
                } catch (error) {
                    console.error('Error:', error);
                    alert('An unexpected error occurred.');
                }
            }
        });
    });
});