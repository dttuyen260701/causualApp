$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.plannings.planning;

    $("#ViewModel_Year").on('change', function () {
        service.getListTargetByYear($("#ViewModel_Year").val())
            .then(function (result) {
                if (result.length > 0) {
                    result.forEach(function (data) {
                        switch (data.month) {
                            case 1:
                                $("#ViewModel_TargetInJan").val(data.revenueTarget)
                                break;
                            case 2:
                                $("#ViewModel_TargetInFeb").val(data.revenueTarget)
                                break;
                            case 3:
                                $("#ViewModel_TargetInMarch").val(data.revenueTarget)
                                break;
                            case 4:
                                $("#ViewModel_TargetInApril").val(data.revenueTarget)
                                break;
                            case 5:
                                $("#ViewModel_TargetInMay").val(data.revenueTarget)
                                break;
                            case 6:
                                $("#ViewModel_TargetInJune").val(data.revenueTarget)
                                break;
                            case 7:
                                $("#ViewModel_TargetInJully").val(data.revenueTarget)
                                break;
                            case 8:
                                $("#ViewModel_TargetInAugust").val(data.revenueTarget)
                                break;
                            case 9:
                                $("#ViewModel_TargetInSep").val(data.revenueTarget)
                                break;
                            case 10:
                                $("#ViewModel_TargetInOct").val(data.revenueTarget)
                                break;
                            case 11:
                                $("#ViewModel_TargetInNov").val(data.revenueTarget)
                                break;
                            case 12:
                                $("#ViewModel_TargetInDec").val(data.revenueTarget)
                                break;
                            default:
                                console.log("not found")
                        }

                    })
                } else {
                    $("#ViewModel_TargetInJan").val(0)
                    $("#ViewModel_TargetInFeb").val(0)
                    $("#ViewModel_TargetInMarch").val(0)
                    $("#ViewModel_TargetInApril").val(0)
                    $("#ViewModel_TargetInMay").val(0)
                    $("#ViewModel_TargetInJune").val(0)
                    $("#ViewModel_TargetInJully").val(0)
                    $("#ViewModel_TargetInAugust").val(0)
                    $("#ViewModel_TargetInSep").val(0)
                    $("#ViewModel_TargetInOct").val(0)
                    $("#ViewModel_TargetInNov").val(0)
                    $("#ViewModel_TargetInDec").val(0)
                }
            })
    })

    $("#ViewModel_Year").trigger('change')
})