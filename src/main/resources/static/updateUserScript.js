document.getElementById("findUserForm").addEventListener("submit", function(event) {
    event.preventDefault();
    const email = document.getElementById("inputEmailFindUser").value;

    fetch(`/api/user/${email}`)
        .then(response => response.json())
        .then(data => {
            if (data) {
                document.getElementById("inputFirstNameUpdateUser").value = data.name;
                document.getElementById("inputLastNameUpdateUser").value = data.surname;
                document.getElementById("inputPhoneNumberUpdateUser").value = data.phoneNumber;
                document.getElementById("inputUsernameUpdateUser").value = data.username;

                if (data.address) {
                    document.getElementById("inputAddressUpdateUser").value = data.address.addressLine || "";
                    document.getElementById("inputCityUpdateUser").value = data.address.city || "";
                    document.getElementById("inputPSCUpdateUser").value = data.address.postCode || "";
                    document.getElementById("inputCountryUpdateUser").value = data.address.country || "";
                } else {
                    console.warn("Adresa nie je dostupná pre tohto používateľa.");
                }

                document.getElementById("updateUserForm").style.display = "block";
            } else {
                alert("User not found.");
            }
        })
        .catch(error => console.error('Error fetching user:', error));
});

document.getElementById("updateUserForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const formData = new FormData(this);
    const userUpdateDTO = {
        email: document.getElementById("inputEmailFindUser").value,
        name: formData.get("firstNameUpdateUser"),
        surname: formData.get("lastNameUpdateUser"),
        username: formData.get("usernameUpdateUser"),
        phoneNumber: formData.get("phoneNumberUpdateUser"),
        addressLine: formData.get("addressLineUpdateUser"),
        city: formData.get("cityUpdateUser"),
        postCode: formData.get("postCodeUpdate"),
        country: formData.get("countryUpdateUser")
    };

    fetch("/api/user/update", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(userUpdateDTO)
    })
        .then(response => {
            if (response.ok) {
                alert("User updated successfully.");
            } else {
                alert("Error updating user.");
            }
        })
        .catch(error => console.error("Error updating user:", error));
});
