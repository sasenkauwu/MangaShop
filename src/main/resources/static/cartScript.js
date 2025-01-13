document.addEventListener("DOMContentLoaded", function () {
    const shippingInfoForm = document.getElementById("shipping-info-form");
    const cartProductsContainer = document.getElementById("cart-products");
    const addProductBtn = document.getElementById("add-product-btn");
    const placeOrderBtn = document.getElementById("place-order-btn");

    loadUserInfo();
    loadCartItems();

    // Load user info for shipping
    function loadUserInfo() {
        fetch("/api/user/meDTO")
            .then(response => response.json())
            .then(user => {
                shippingInfoForm.querySelector("#name").value = `${user.name} ${user.surname}`;
                shippingInfoForm.querySelector("#address").value = user.addressLine;
                shippingInfoForm.querySelector("#city").value = user.city;
                shippingInfoForm.querySelector("#postcode").value = user.postCode;
                shippingInfoForm.querySelector("#country").value = user.country;
                shippingInfoForm.querySelector("#phone").value = user.phoneNumber;
            })
            .catch(error => console.error("Error loading user info:", error));
    }

    // Load cart items
    function loadCartItems() {
        fetch("/api/cart/getAll")
            .then(response => response.json())
            .then(products => {
                console.log("Loaded products:", products); // Debug
                cartProductsContainer.innerHTML = products.map(product => `
                <div class="d-flex align-items-center border-bottom p-2">
                    <img src="${product.imageURL}" alt="${product.title}" class="img-fluid me-3" style="width: 80px; height: 80px;">
                    <div class="flex-grow-1 ml-5">
                        <h6>${product.title}</h6>
                        <p class="mb-1">Price: $${product.price.toFixed(2)}</p>
                        <p class="mb-0">Quantity: ${product.quantity}</p>
                    </div>
                    <button class="btn btn-dark btn-sm delete-product-btn ml-auto" data-product-id="${product.productId}">
                        X
                    </button>
                </div>
            `).join('');

                // Attach delete event listeners
                document.querySelectorAll('.delete-product-btn').forEach(button => {
                    button.addEventListener('click', function () {
                        const productId = this.dataset.productId;
                        deleteProduct(productId);
                    });
                });

                const totalPriceElement = document.getElementById("total-price");
                totalPriceElement.textContent = products[0]?.totalPrice.toFixed(2) || "0.00";
            })
            .catch(error => console.error("Error loading cart items:", error));
    }


    // Add product to cart
    if (addProductBtn) {
        addProductBtn.addEventListener("click", function () {
            const productId = window.location.pathname.split("/")[2];

            if (productId) {
                fetch(`/api/cart/add/${productId}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
                        if (response.ok) {
                            loadCartItems();
                        } else {
                            return response.text().then(text => {
                                throw new Error(text || "Error adding product to cart.");
                            });
                        }
                    })
                    .catch(error => {
                        console.error("Error:", error);
                        alert("Failed to add product to cart. Please try again.");
                    });
            } else {
                console.error("Product ID not found in URL.");
                alert("Invalid product. Unable to add to cart.");
            }
        });
    }

    if (placeOrderBtn) {
        placeOrderBtn.addEventListener("click", function () {
            // Send request to clear the cart
            fetch("/api/cart/clear", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (response.ok) {
                        loadCartItems();
                    } else {
                        return response.text().then(text => {
                            throw new Error(text || "Error clearing the cart.");
                        });
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    alert("Failed to place order. Please try again.");
                });
        });
    }


    function deleteProduct(productId) {
        fetch(`/api/cart/delete/${productId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                if (response.ok) {
                    loadCartItems(); // Refresh the cart items
                } else {
                    return response.text().then(text => {
                        throw new Error(text || "Error removing product from cart.");
                    });
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("Failed to remove product from cart. Please try again.");
            });
    }
});
