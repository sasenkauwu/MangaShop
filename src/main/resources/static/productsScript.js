document.getElementById('searchInput').addEventListener('input', function () {
    const query = this.value;
    fetch(`/products/search?query=${query}`)
        .then(response => response.text())
        .then(html => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;
            const productContainer = tempDiv.querySelector('.row-cols-1');
            document.querySelector('.row-cols-1').innerHTML = productContainer.innerHTML;
            toggleNoMatchesMessage();
        });
});

function filterByCategory(category) {
    fetch(`/products/category?category=${category}`)
        .then(response => response.text())
        .then(html => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;
            const productContainer = tempDiv.querySelector('.row-cols-1');
            document.querySelector('.row-cols-1').innerHTML = productContainer.innerHTML;
            toggleNoMatchesMessage();
        });
}


function toggleNoMatchesMessage() {
    const products = document.querySelectorAll('.product-card');
    const message = document.getElementById('noMatchesMessage');
    message.style.display = products.length ? 'none' : 'block';
}
