/**
 * Sidebar and Layout Toggle Script
 * -----------------------------------
 * This script handles the interaction to show/hide the sidebar navigation.
 * It is fully isolated and waits for user interaction on the toggle button.
 *
 * HOW IT WORKS:
 * 1. We grab the main `<body>` element, the sidebar `<aside>`, and the toggle `<button>`.
 * 2. When the toggle button is clicked, we add/remove the CSS class `sidebar-collapsed`
 *    on the `<body>`.
 * 3. The CSS file relies on `body.sidebar-collapsed` to slide the sidebar off-screen
 *    or hide it completely on mobile screens.
 * 4. We also update ARIA attributes for accessibility so screen readers know the state.
 */
(function () {
    // 1. Target the elements in the DOM
    var body = document.body;
    var sidebar = document.getElementById("sidebar-panel");
    var toggleBtn = document.getElementById("sidebar-menu-toggle");

    // If for some reason the sidebar or button doesn't exist on this page, exit early to avoid errors.
    if (!sidebar || !toggleBtn) {
        return;
    }

    // 2. Add a click event listener to the menu button
    toggleBtn.addEventListener("click", function () {
        // Toggle the class and store the result (true if class was added, false if removed)
        var collapsed = body.classList.toggle("sidebar-collapsed");

        // 3. Update accessibility (ARIA) hints for screen readers
        // If sidebar is 'collapsed' (true), then expanded is 'false', and vice versa.
        toggleBtn.setAttribute("aria-expanded", String(!collapsed));
        
        // Update the spoken label of the button
        toggleBtn.setAttribute(
            "aria-label",
            collapsed ? "Show sidebar" : "Hide sidebar"
        );

        // Hide the sidebar element from accessibility APIs when it's off-screen
        if (collapsed) {
            sidebar.setAttribute("aria-hidden", "true");
        } else {
            sidebar.removeAttribute("aria-hidden");
        }
    });
})();
