document.querySelectorAll(".chat-room").forEach(room => {
    room.addEventListener("mouseenter", () => {
        room.classList.add("hover");
    });
    room.addEventListener("mouseleave", () => {
        room.classList.remove("hover");
    });
});