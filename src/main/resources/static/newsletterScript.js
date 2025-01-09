document.getElementById("newsletterBTN").addEventListener("click", async () => {
    const emailInput = document.getElementById("newsletterEmail");
    const email = emailInput.value.trim();

    if (!email) {
        alert("Please enter a valid email address.");
        return;
    }

    try {
        const response = await fetch(`/api/user/subscribe/${encodeURIComponent(email)}`, {
            method: "POST",
        });

        if (response.ok) {
            const message = await response.text();
            alert(message);
            emailInput.value = "";
        } else {
            const error = await response.text();
            alert(`Failed to subscribe: ${error}`);
        }
    } catch (error) {
        console.error("Error subscribing to newsletter:", error);
        alert("An error occurred. Please try again later.");
    }
});
