//------------------------------------------ DELETE USER SCRIPT ---------------------------------------------
function showError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    errorElement.innerText = message;
}

function clearErrors() {
    const errorElements = document.querySelectorAll('.text-danger');
    errorElements.forEach(error => error.innerText = '');
}

document.addEventListener('DOMContentLoaded', () => {
    const deleteUserForm = document.getElementById('deleteUserForm');
    const emailInput = document.getElementById('inputEmailDelete');
    const passwordInput = document.getElementById('inputPasswordDelete');

    const errorMessages = {
        emailRequired: "Email cannot be empty.",
        emailInvalid: "Invalid email format.",
        passwordRequired: "Password cannot be empty.",
        deleteFailed: "Error deleting user. Please check your details.",
    };

    const emailError = document.createElement('small');
    emailError.classList.add('text-danger');
    emailInput.parentElement.appendChild(emailError);

    const passwordError = document.createElement('small');
    passwordError.classList.add('text-danger');
    passwordInput.parentElement.appendChild(passwordError);

    const formError = document.createElement('div');
    formError.classList.add('alert', 'alert-danger', 'mt-3', 'd-none');
    deleteUserForm.appendChild(formError);


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


    deleteUserForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        clearErrors();

        if (!validateForm()) {
            return;
        }

        const deleteData = {
            email: emailInput.value.trim(),
            password: passwordInput.value.trim(),
        };

        try {
            const response = await fetch('/api/user/delete', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(deleteData),
            });

            if (response.ok) {
                alert("User deleted successfully.");
                window.location.href = '/';
            } else if (response.status === 400) {
                const errorMessage = await response.text();
                formError.textContent = errorMessage || errorMessages.deleteFailed;
                formError.classList.remove('d-none');
            } else {
                throw new Error('Unexpected error occurred.');
            }
        } catch (error) {
            console.error('Error:', error);
            formError.textContent = errorMessages.deleteFailed;
            formError.classList.remove('d-none');
        }
    });
});