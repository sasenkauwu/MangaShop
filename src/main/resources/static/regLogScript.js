//-------------------------------------------REGISTRACIA----------------------------------------------------------
function submitRegistrationForm() {
    event.preventDefault();
    clearErrors();

    let isValid = true;

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

    if (!firstName) {
        showError("inputNameError", "First name is required.");
        isValid = false;
    }

    if (!lastName) {
        showError("inputSurnameError", "Last name is required.");
        isValid = false;
    }

    const phonePattern = /^[0-9]{10}$/;
    if (!phoneNumber) {
        showError("inputPhoneError", "Phone number is required.");
        isValid = false;
    } else if (!phonePattern.test(phoneNumber)) {
        showError("inputPhoneError", "Phone number must be 10 digits.");
        isValid = false;
    }

    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!email) {
        showError("inputEmailError", "E-mail is required.");
        isValid = false;
    } else if (!emailPattern.test(email)) {
        showError("inputEmailError", "E-mail format is invalid.");
        isValid = false;
    }

    if (!username) {
        showError("inputUsernameError", "Username is required.");
        isValid = false;
    }

    if (!password) {
        showError("inputPasswordError", "Password is required.");
        isValid = false;
    } else if (password.length < 10 || password.length > 25) {
        showError("inputPasswordError", "Password must be between 10 and 25 characters.");
        isValid = false;
    }

    if (!addressLine) {
        showError("inputAddressError", "Address is required.");
        isValid = false;
    }

    if (!city) {
        showError("inputCityError", "City is required.");
        isValid = false;
    }

    const postCodePattern = /^[0-9]{5}$/;
    if (!postCode) {
        showError("inputPSCError", "Postcode is required.");
        isValid = false;
    } else if (!postCodePattern.test(postCode)) {
        showError("inputPSCError", "Postcode must be exactly 5 digits.");
        isValid = false;
    }

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
                window.location.href = '/login';
                return response.text();
            })
            .catch(error => {
                console.error("Error:", error);
                alert("Something went wrong, please try again later.");
            });
    }

    return isValid;
}

//---------------------------------------LOGIN----------------------------------------------------------------------------


function showError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    errorElement.innerText = message;
}

function clearErrors() {
    const errorElements = document.querySelectorAll('.text-danger');
    errorElements.forEach(error => error.innerText = '');
}


document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('inputUsernameLogin');
    const passwordInput = document.getElementById('inputPasswordLogin');

    const errorMessages = {
        emailRequired: "Email cannot be empty.",
        emailInvalid: "Invalid email format.",
        passwordRequired: "Password cannot be empty.",
        loginFailed: "Email or password is wrong.",
    };

    const emailError = document.createElement('small');
    emailError.classList.add('text-danger');
    emailInput.parentElement.appendChild(emailError);

    const passwordError = document.createElement('small');
    passwordError.classList.add('text-danger');
    passwordInput.parentElement.appendChild(passwordError);

    const formError = document.createElement('div');
    formError.classList.add('alert', 'alert-danger', 'mt-3', 'd-none');
    loginForm.appendChild(formError);

    const validateEmail = (email) => {
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(email);
    };

    const validateForm = () => {
        let valid = true;

        if (!emailInput.value.trim()) {
            emailError.textContent = errorMessages.emailRequired;
            valid = false;
        } else if (!validateEmail(emailInput.value.trim())) {
            emailError.textContent = errorMessages.emailInvalid;
            valid = false;
        } else {
            emailError.textContent = "";
        }

        if (!passwordInput.value.trim()) {
            passwordError.textContent = errorMessages.passwordRequired;
            valid = false;
        } else {
            passwordError.textContent = "";
        }

        return valid;
    };

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        if (!validateForm()) {
            return;
        }

        const loginData = {
            email: emailInput.value.trim(),
            password: passwordInput.value.trim(),
        };

        try {
            const response = await fetch('/api/user/login', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData),
            });

            if (response.ok) {
                window.location.href = '/';
            } else if (response.status === 401) {
                formError.textContent = errorMessages.loginFailed;
                formError.classList.remove('d-none');
            } else {
                throw new Error('Unexpected error occurred.');
            }
        } catch (error) {
            console.error('Error:', error);
            formError.textContent = 'Something went wrong. Please try again later. ' + error;
            formError.classList.remove('d-none');
        }
    });
});

function logout() {
    fetch('/api/user/logout', {
        method: 'GET',
        credentials: 'same-origin'
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/login';
            } else {
                alert('Error logging out');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
