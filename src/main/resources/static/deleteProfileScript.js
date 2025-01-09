document.getElementById('deleteUserBtn').addEventListener('click', async function () {
    const password = document.getElementById('inputPasswordDelete').value;
    const errorField = document.getElementById('deleteUserError');

    errorField.innerText = ''; // Vyčisti predchádzajúce chyby

    if (!password) {
        errorField.innerText = 'Please enter your password.';
        return;
    }

    try {
        const userResponse = await fetch('/api/user/me');
        if (!userResponse.ok) {
            throw new Error('Failed to fetch current user data.');
        }

        const userData = await userResponse.json();
        const email = userData.email;

        const deleteResponse = await fetch('/api/user/delete', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        if (deleteResponse.ok) {
            alert('Your account has been successfully deleted.');
            logout();
            window.location.href = '/';
        } else {
            //const errorText = await deleteResponse.text();
            errorField.innerText = 'Invalid password provided.' || 'Failed to delete account.';
        }
    } catch (error) {
        console.error('Error:', error);
        errorField.innerText = 'An unexpected error occurred.';
    }
});

function logout() {
    fetch('/api/user/logout', {
        method: 'GET',
        credentials: 'same-origin'
    })
        .then(response => {
            if (response.ok) {

                            } else {
                alert('Error logging out');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
