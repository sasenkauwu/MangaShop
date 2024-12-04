const validateEmail = (email) => {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
};

function showError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    errorElement.innerText = message;
}

function clearErrors() {
    const errorElements = document.querySelectorAll('.text-danger');
    errorElements.forEach(error => error.innerText = '');
}

document.getElementById('changePasswordForm').addEventListener('submit', async (event) => {
    event.preventDefault();
    clearErrors();

    const email = document.getElementById('inputEmailChangePswd').value.trim();
    const oldPassword = document.getElementById('inputPasswordChangePswdOld').value.trim();
    const newPassword = document.getElementById('inputPasswordChangePswdNew').value.trim();

    let isValid = true;

    if (!email || !validateEmail(email)) {
        showError('emailError', 'Invalid email format.');
        isValid = false;
    }
    if (!oldPassword) {
        showError('oldPasswordError', 'Old password is required.');
        isValid = false;
    }
    if (!newPassword) {
        showError('newPasswordError', 'New password is required.');
        isValid = false;
    }

    if (!isValid) return;

    const data = {
        email: email,
        oldPassword: oldPassword,
        newPassword: newPassword
    };

    try {
        const response = await fetch('/api/user/changePassword', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            alert('Password changed successfully!');
        } else {
            const errorMessage = await response.text();
            alert(`Error: ${errorMessage}`);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while changing the password.');
    }
});