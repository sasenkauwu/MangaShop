
function submitRegistrationForm() {
    // Clear any previous error messages
    event.preventDefault();
    clearErrors();

    let isValid = true;

    // Get form field values
    const firstName = document.getElementById("inputFirstName").value;
    const lastName = document.getElementById("inputLastName").value;
    const phoneNumber = document.getElementById("inputPhoneNumber").value;
    const email = document.getElementById("inputEmail").value;
    const username = document.getElementById("inputUsername").value;
    const password = document.getElementById("inputPassword").value;
    const addressLine = document.getElementById("inputAddress").value;
    const city = document.getElementById("inputCity").value;
    const postCode = document.getElementById("inputPSC").value;
    const country = document.getElementById("inputCountry").value;

    // Validate First Name
    if (!firstName) {
        showError("inputNameError", "First name is required.");
        isValid = false;
    }

    // Validate Last Name
    if (!lastName) {
        showError("inputSurnameError", "Last name is required.");
        isValid = false;
    }

    // Validate Phone Number (simple pattern)
    const phonePattern = /^[0-9]{10}$/;
    if (!phoneNumber) {
        showError("inputPhoneError", "Phone number is required.");
        isValid = false;
    } else if (!phonePattern.test(phoneNumber)) {
        showError("inputPhoneError", "Phone number must be 10 digits.");
        isValid = false;
    }

    // Validate Email
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!email) {
        showError("inputEmailError", "E-mail is required.");
        isValid = false;
    } else if (!emailPattern.test(email)) {
        showError("inputEmailError", "E-mail format is invalid.");
        isValid = false;
    }

    // Validate Username
    if (!username) {
        showError("inputUsernameError", "Username is required.");
        isValid = false;
    }

    // Validate Password (length check)
    if (!password) {
        showError("inputPasswordError", "Password is required.");
        isValid = false;
    } else if (password.length < 10 || password.length > 25) {
        showError("inputPasswordError", "Password must be between 10 and 25 characters.");
        isValid = false;
    }

    // Validate Address
    if (!addressLine) {
        showError("inputAddressError", "Address is required.");
        isValid = false;
    }

    // Validate City
    if (!city) {
        showError("inputCityError", "City is required.");
        isValid = false;
    }

    // Validate Post Code (exactly 5 digits)
    const postCodePattern = /^[0-9]{5}$/;
    if (!postCode) {
        showError("inputPSCError", "Postcode is required.");
        isValid = false;
    } else if (!postCodePattern.test(postCode)) {
        showError("inputPSCError", "Postcode must be exactly 5 digits.");
        isValid = false;
    }

    // Validate Country
    if (!country) {
        showError("inputCountryError", "Country is required.");
        isValid = false;
    }

    if (isValid) {
        let userData = {
            email: email,
            name: firstName,
            surname: lastName,
            username: username,
            password: password,
            phoneNumber: phoneNumber,
            addressLine: addressLine,
            city: city,
            postCode: postCode,
            country: country
        };

        /*fetch('/api/user/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // Correct content type for JSON
            },
            body: JSON.stringify(userData) // Send data as JSON string
        })
            .then(response => response.json())
            .then(data => {
                alert(data.message || "User registered successfully");
            })
            .catch(error => {
                console.error("Error:", error);
                alert("Something went wrong, please try again later.");
            });*/

        fetch('/api/user/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.text(); // Parse the response as plain text
            })
            .then(data => {
                alert(data || "User registered successfully");

            })
            .catch(error => {
                console.error("Error:", error);
                alert("Something went wrong, please try again later.");
            });
    }


    // Prevent form submission if any validation failed
    return isValid;
}


// Function to show error messages
function showError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    errorElement.innerText = message;
}

// Function to clear previous error messages
function clearErrors() {
    const errorElements = document.querySelectorAll('.text-danger');
    errorElements.forEach(error => error.innerText = '');
}
/*
function handleLoginSubmit(event) {
    localStorage.setItem()
    event.preventDefault();  // Prevent the default form submission

    const username = document.getElementById("inputUsernameLogin").value;
    const password = document.getElementById("inputPasswordLogin").value;

    // Prepare the data to be sent in the POST request
    const loginData = {
        username: username,
        password: password
    };

    // Send the login data to the backend using fetch
    fetch("/api/user/login", {
        method: "POST",
        headers: {
            "Authorization:"
            "Content-Type": "application/json"
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (response.ok) {
                localStorage.setItem()
                // If login is successful, redirect the user
                alert("Login successful!");
                window.location.href = "/profile"; // Redirect to the profile page (or home page)
            } else {
                // Handle errors (e.g., invalid login credentials)
                response.text().then(errorMessage => {
                    alert("Login failed: " + errorMessage);
                });
            }
        })
        .catch(error => {
            alert("An error occurred: " + error.message);
        });
}

// Attach the handleLoginSubmit function to the login form
document.getElementById("loginForm").addEventListener("submit", handleLoginSubmit);*/

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('inputUsernameLogin');
    const passwordInput = document.getElementById('inputPasswordLogin');

    // Správy o chybách
    const errorMessages = {
        emailRequired: "Email cannot be empty.",
        emailInvalid: "Invalid email format.",
        passwordRequired: "Password cannot be empty.",
        loginFailed: "Email or password is wrong.",
    };

    // Vytvoríme miesto pre chyby
    const emailError = document.createElement('small');
    emailError.classList.add('text-danger');
    emailInput.parentElement.appendChild(emailError);

    const passwordError = document.createElement('small');
    passwordError.classList.add('text-danger');
    passwordInput.parentElement.appendChild(passwordError);

    const formError = document.createElement('div');
    formError.classList.add('alert', 'alert-danger', 'mt-3', 'd-none');
    loginForm.appendChild(formError);

    // Validácia emailu
    const validateEmail = (email) => {
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(email);
    };

    // Validácia formulára
    const validateForm = () => {
        let valid = true;

        // Kontrola emailu
        if (!emailInput.value.trim()) {
            emailError.textContent = errorMessages.emailRequired;
            valid = false;
        } else if (!validateEmail(emailInput.value.trim())) {
            emailError.textContent = errorMessages.emailInvalid;
            valid = false;
        } else {
            emailError.textContent = "";
        }

        // Kontrola hesla
        if (!passwordInput.value.trim()) {
            passwordError.textContent = errorMessages.passwordRequired;
            valid = false;
        } else {
            passwordError.textContent = "";
        }

        return valid;
    };

    // Spracovanie odoslania formulára
    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault(); // Zamedzíme predvolené spracovanie formulára

        // Validácia
        if (!validateForm()) {
            return;
        }

        // Dáta pre backend
        const loginData = {
            email: emailInput.value.trim(),
            password: passwordInput.value.trim(),
        };

        try {
            // Pošleme dáta na backend
            const response = await fetch('/api/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData),
            });

            if (response.ok) {
                // Pri úspechu presmerujeme užívateľa
                window.location.href = '/'; // Zmeňte na URL, kam chcete presmerovať po prihlásení
            } else if (response.status === 401) {
                // Pri nesprávnych údajoch zobrazíme správu
                formError.textContent = errorMessages.loginFailed;
                formError.classList.remove('d-none');
            } else {
                throw new Error('Unexpected error occurred.');
            }
        } catch (error) {
            console.error('Error:', error);
            formError.textContent = 'Something went wrong. Please try again later.';
            formError.classList.remove('d-none');
        }
    });
});
