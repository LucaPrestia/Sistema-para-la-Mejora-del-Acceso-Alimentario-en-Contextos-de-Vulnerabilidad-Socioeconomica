document.addEventListener("DOMContentLoaded", function () {
    const steps = document.querySelectorAll(".step");
    const nextButtons = document.querySelectorAll(".next-step");
    const previousButtons = document.querySelectorAll(".previous-step");
    const progressBar = document.getElementById("progressBar");

    let currentStep = 0;

    function showStep(stepIndex) {
        steps.forEach((step, index) => {
            step.classList.toggle("d-none", index !== stepIndex);
            step.classList.toggle("active", index === stepIndex);
        });

        const progress = ((stepIndex + 1) / steps.length) * 100;
        progressBar.style.width = `${progress}%`;
        progressBar.setAttribute("aria-valuenow", progress);
    }

    nextButtons.forEach((button) => {
        button.addEventListener("click", () => {
            if (currentStep < steps.length - 1) {
                currentStep++;
                showStep(currentStep);
            }
        });
    });

    previousButtons.forEach((button) => {
        button.addEventListener("click", () => {
            if (currentStep > 0) {
                currentStep--;
                showStep(currentStep);
            }
        });
    });

    showStep(currentStep);
});