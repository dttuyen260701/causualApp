$(function () {
    const l = abp.localization.getResource('CasualManager');

    var image = document.getElementById('iconImage');
    var Cropper = window.Cropper;
    var cropper;
    var options = {
        aspectRatio: 1 / 1,
        zoomable: false,
        viewMode: 1,
        responsive: false,
        guides: false,
        background: false,
        autoCropArea: 1,
    };
    $('#FileIcon').on('change', function () {
        readURL(this);
        console.log(cropper)
    });

    $('#CloseImage').on('click', function () {
        document.getElementById('FileIcon').value = null;
        image.src = "/upload_images/type_of_job/iconDefault.png";
        if (cropper) {
            cropper.destroy();
        }
        check = null;
        $("#checkImageCrop").prop('hidden', true);
    });

    var check;
    $('#checkImageCrop').on("click", function () {
        crop = cropper.crop();
        crop.getCroppedCanvas().toBlob((blob) => {
            let file = new File([blob], "IconTypeOfJob.jpg", { type: "image/jpeg" })
            const dT = new DataTransfer();
            dT.items.add(file);
            document.getElementById('FileIcon').files = dT.files;
            check = true;
        })
        image.src = crop.getCroppedCanvas().toDataURL();
        cropper.destroy();
        $("#checkImageCrop").prop('hidden', true);
    })

    $('#save').on('click', function () {
        if (!check) {
            document.getElementById('FileIcon').value = null;
        }
    })

    let readURL = function (input) {
        if (/^image\/\w+/.test(input.files[0].type)) {
            image.src = URL.createObjectURL(input.files[0]);
            if (cropper) {
                cropper.destroy();
            }
            cropper = new Cropper(image, options);
            check = false;
            $("#checkImageCrop").prop('hidden', false);
        }
    }
})