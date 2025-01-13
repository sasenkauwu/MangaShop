function showError(inputId, message) {
    document.getElementById(inputId).innerText = message;
}

function clearErrors() {
    const errorFields = document.querySelectorAll('small.text-danger');
    errorFields.forEach((field) => field.innerText = '');
}

function loadCurrentUserData() {
    fetch('/api/user/meDTO')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user data');
            }
            return response.json();
        })
        .then(data => {
            if (data) {
                document.getElementById("userEmail").innerText = data.email || '';
                document.getElementById("name").value = data.name || '';
                document.getElementById("surname").value = data.surname || '';
                document.getElementById("phoneNumber").value = data.phoneNumber || '';
                document.getElementById("username").value = data.username || '';

                    document.getElementById("addressLine").value = data.addressLine || '';
                    document.getElementById("city").value = data.city || '';
                    document.getElementById("postCode").value = data.postCode || '';
                    document.getElementById("country").value = data.country || '';
            } else {
                console.warn("Žiadne údaje pre prihláseného používateľa.");
            }
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            alert('Failed to load user data. Please try again later.');
        });
}

function validateForm(userData) {
    let isValid = true;
    clearErrors();

    if (!userData.name) {
        showError("nameError", "First name is required.");
        isValid = false;
    }

    if (!userData.surname) {
        showError("surnameError", "Last name is required.");
        isValid = false;
    }

    const phonePattern = /^[0-9]{10}$/;
    if (!userData.phoneNumber) {
        showError("phoneError", "Phone number is required.");
        isValid = false;
    } else if (!phonePattern.test(userData.phoneNumber)) {
        showError("phoneError", "Phone number must be 10 digits.");
        isValid = false;
    }

    if (!userData.username) {
        showError("usernameError", "Username is required.");
        isValid = false;
    }

    if (!userData.addressLine) {
        showError("addressError", "Address line is required.");
        isValid = false;
    }

    if (!userData.city) {
        showError("cityError", "City is required.");
        isValid = false;
    }

    const postCodePattern = /^[0-9]{5}$/;
    if (!userData.postCode) {
        showError("postCodeError", "Postcode is required.");
        isValid = false;
    } else if (!postCodePattern.test(userData.postCode)) {
        showError("postCodeError", "Postcode must be exactly 5 digits.");
        isValid = false;
    }

    if (!userData.country) {
        showError("countryError", "Country is required.");
        isValid = false;
    }

    return isValid;
}

document.getElementById('updateButton').addEventListener('click', function () {
    const userData = {
        email: document.getElementById('userEmail').innerText,
        name: document.getElementById('name').value,
        surname: document.getElementById('surname').value,
        phoneNumber: document.getElementById('phoneNumber').value,
        username: document.getElementById('username').value,
        addressLine: document.getElementById('addressLine').value,
        city: document.getElementById('city').value,
        postCode: document.getElementById('postCode').value,
        country: document.getElementById('country').value
    };

    if (validateForm(userData)) {
        fetch('/api/user/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
            .then(response => {
                if (response.ok) {
                } else {
                    return response.json();
                }
            })
            .then(errors => {
                if (errors) {
                    errors.forEach(error => {
                        showError(`${error.field}Error`, error.defaultMessage);
                    });
                }
            })
            .catch(error => {
                console.error('Error updating user data:', error);
                alert('Failed to update user information. Please try again.');
            });
    }
});

document.addEventListener('DOMContentLoaded', loadCurrentUserData);
