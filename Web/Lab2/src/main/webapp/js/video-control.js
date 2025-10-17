document.addEventListener("DOMContentLoaded", () => {
    const video = document.querySelector(".video-container video");
    if (!video) return;

    let fadeInterval = null;

    const fadeVolume = (targetVolume) => {
        if (fadeInterval) clearInterval(fadeInterval);

        fadeInterval = setInterval(() => {
            const delta = 0.05; // шаг изменения громкости
            if (Math.abs(video.volume - targetVolume) < delta) {
                video.volume = targetVolume;
                clearInterval(fadeInterval);
                fadeInterval = null;
            } else {
                video.volume += video.volume < targetVolume ? delta : -delta;
            }
        }, 50); // каждые 50 мс
    };

    // Видео изначально без звука
    video.muted = false;
    video.volume = 0;

    video.addEventListener("mouseenter", () => fadeVolume(1)); // плавно включаем звук
    video.addEventListener("mouseleave", () => fadeVolume(0)); // плавно выключаем звук
});
