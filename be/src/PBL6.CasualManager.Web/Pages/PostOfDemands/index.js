$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.postOfDemands.postOfDemand;

    const dataTable = $('#post-of-demand-datatable').DataTable(abp.libs.datatables.normalizeConfiguration({
        processing: true,
        responsive: true,
        serverSide: true,
        paging: true,
        searching: false,
        autoWidth: true,
        fixedColumns: true,
        fixedHeader: true,
        bLengthChange: true,
        scrollCollapse: true,
        ordering: false,
        ajax: abp.libs.datatables.createAjax(service.getListSearch, GetValueSearch),
        columnDefs: [
            {
                title: l('PostOfDemand:CustomerName'),
                data: "customerName"
            },
            {
                title: l('PostOfDemand:JobName'),
                data: "jobName"
            },
            {
                title: l('PostOfDemand:EndDate'),
                data: "endTime",
                render: function (data) {
                    return luxon
                        .DateTime
                        .fromISO(data, {
                            locale: abp.localization.currentCulture.name
                        }).toFormat('dd/MM/yyyy');
                },
            },
            {
                title: l('PostOfDemand:Description'),
                data: "description"
            },
            {
                title: l('PostOfDemand:Note'),
                data: "note"
            },
            {
                title: l('PostOfDemand:DistanceUpLoad'),
                data: "distanceUpLoad",
                render: function (data) {
                    return l(data)
                }
            },
            {
                title: l('PostOfDemand:CustomerAddressDetail'),
                data: "customerAddressDetail"
            },
            {
                title: l('PostOfDemand:IsActive'),
                data: "isActive",
                render: function (data) {
                    return data ? `<span class="fa fa-check-circle is-checked"></span>` : `<span class="fa fa-times-circle isnt-checked"></span>`
                },
                class: "text-center"
            },
            {
                title: l('Delete'),
                rowAction: {
                    items: [{
                        text: '<i class="fa fa-trash-o danger"></i>',
                        visible: abp.auth.isGranted('CasualManager.PostOfDemand.Delete'),
                        confirmMessage: function (data) {
                            return l('Common:DeletionConfirmationMessage', data.record.jobName);
                        },
                        action: function (data) {
                            service.delete(data.record.id)
                                .then(function () {
                                    abp.notify.info(l('Common:SuccessfullyDeleted'));
                                    dataTable.ajax.reload();
                                });
                        },
                        displayNameHtml: true,
                    }]
                },
                width: "5%",
                class: "text-center"
            },
        ],
    })
    );

    $('#search').click(function () {
        Search();
    });

    function Search() {
        dataTable.ajax.reload();
    }

    function GetValueSearch() {
        return {
            'filterCustomer': $("#ViewModel_FilterCustomer").val()
        };
    }
});