$(function () {
    const l = abp.localization.getResource('BKCITSystem');

    $("#ViewModel_TypeOfJobId").on("change", function () {
        const selectJobInfo = $('#ViewModel_JobInfoId')
        pBL6.casualManager.jobInfos.jobInfo
            .getListByTypeOfJob($("#ViewModel_TypeOfJobId").val())
            .then(function (result) {
                selectJobInfo.empty();
                result.forEach(function (data) {
                    selectJobInfo.append($('<option>', {
                        value: data.id,
                        text: `${data.name} - ${Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data.prices) }`
                    }));
                });
            })
    })
})