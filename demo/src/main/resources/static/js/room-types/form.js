const additionalPhotos = document.getElementById("additional-photos");
const addPhotoButton = document.getElementById("add-photo");

if (additionalPhotos && addPhotoButton) {
  let photoIndex = additionalPhotos.querySelectorAll("input").length;

  addPhotoButton.addEventListener("click", () => {
    const row = document.createElement("div");
    row.className = "photo-field";
    row.innerHTML = `<span class="photo-field__number">${photoIndex + 1}</span><div><label class="tw-sr-only" for="photo-${photoIndex}">Additional photo URL</label><input class="portal-control tw-w-full tw-px-4 tw-py-3" type="url" id="photo-${photoIndex}" name="secondaryPhotos[${photoIndex}]" placeholder="https://example.com/additional-room-type-photo.jpg" /></div>`;
    additionalPhotos.appendChild(row);
    photoIndex += 1;
  });
}
