$(function () {
    const l = abp.localization.getResource('BKCITSystem');

    $("#ViewModel_ProvinceId").on("change", function () {
        const selectDistrict = $('#ViewModel_DistrictId')
        pBL6.casualManager.addresses.address
            .getDistricts($("#ViewModel_ProvinceId").val())
            .then(function (result) {
                selectDistrict.empty();
                result.forEach(function (data) {
                    selectDistrict.append($('<option>', {
                        value: data.id,
                        text: data.name
                    }));
                });
                $("#ViewModel_DistrictId").trigger("change")
            })
    })

    $("#ViewModel_DistrictId").on("change", function () {
        const selectWard = $('#ViewModel_WardId')
        pBL6.casualManager.addresses.address
            .getWards($("#ViewModel_DistrictId").val())
            .then(function (result) {
                selectWard.empty();
                result.forEach(function (data) {
                    selectWard.append($('<option>', {
                        value: data.id,
                        text: data.name
                    }));
                });
            });
    })
})