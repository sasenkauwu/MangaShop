document.getElementById('changePasswordBtn').addEventListener('click', async () => {
    const oldPassword = document.getElementById('inputPasswordChangePswdOld').value;
    const newPassword = document.getElementById('inputPasswordChangePswdNew').value;
    const repeatPassword = document.getElementById('inputPasswordChangePswdNew2').value;

    document.getElementById('errorOldPassword').innerText = '';
    document.getElementById('errorNewPassword').innerText = '';
    document.getElementById('errorRepeatPassword').innerText = '';

    let valid = true;

    if (oldPassword.trim() === '') {
        document.getElementById('errorOldPassword').innerText = 'Old password is required.';
        valid = false;
    }

    if (newPassword.length < 10 || newPassword.length > 25) {
        document.getElementById('errorNewPassword').innerText = 'New password must be between 10 and 25 characters.';
        valid = false;
    }

    if (repeatPassword.length < 10 || repeatPassword.length > 25) {
        document.getElementById('errorRepeatPassword').innerText = 'New password must be between 10 and 25 characters.';
        valid = false;
    }

    if (newPassword !== repeatPassword) {
        document.getElementById('errorRepeatPassword').innerText = 'Passwords do not match.';
        valid = false;
    }

    if (!valid) {
        return;
    }

    try {
        const userResponse = await fetch('/api/user/me');
        if (!userResponse.ok) {
            throw new Error('Failed to fetch user info.');
        }
        const user = await userResponse.json();
        const email = user.email;

        const payload = {
            email: email,
            oldPassword: oldPassword,
            newPassword: newPassword
        };

        const response = await fetch('/api/user/changePassword', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            window.location.href = '/profile';
        } else {
            document.getElementById('errorOldPassword').innerText = 'You entered an incorrect old password.';

        }
    } catch (error) {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    }
});
