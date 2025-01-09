document.getElementById('submitBtn').addEventListener('click', async function () {
    const form = document.getElementById('productForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/api/product/save', {
            method: 'POST',
            body: formData,
        });

        if (response.ok) {
            const message = await response.text();
            alert('Success: ' + message);
        } else {
            const error = await response.text();
            alert('Error: ' + error);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An unexpected error occurred.');
    }
});