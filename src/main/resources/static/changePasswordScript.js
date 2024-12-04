const validateEmail = (email) => {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
};

// Function to show error messages
function showError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    errorElement.innerText = message;
}

// Clear error messages
function clearErrors() {
    const errorElements = document.querySelectorAll('.text-danger');
    errorElements.forEach(error => error.innerText = '');
}

// Handle form submission
document.getElementById('changePasswordForm').addEventListener('submit', async (event) => {
    event.preventDefault(); // Prevent default form submission
    clearErrors(); // Clear previous error messages

    // Get form values
    const email = document.getElementById('inputEmailChangePswd').value.trim();
    const oldPassword = document.getElementById('inputPasswordChangePswdOld').value.trim();
    const newPassword = document.getElementById('inputPasswordChangePswdNew').value.trim();

    // Validate inputs
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

    // Prepare data for submission
    const data = {
        email: email,
        oldPassword: oldPassword,
        newPassword: newPassword
    };

    try {
        // Send PUT request to change password
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