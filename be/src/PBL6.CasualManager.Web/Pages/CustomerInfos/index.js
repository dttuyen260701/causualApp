$(function () {
    const l = abp.localization.getResource('CasualManager');

    const service = pBL6.casualManager.customerInfos.customerInfo;
    const createModal = new abp.ModalManager(abp.appPath + 'CustomerInfos/CreateModal');
    const editModal = new abp.ModalManager(abp.appPath + 'CustomerInfos/EditModal');

    const dataTable = $('#customer-datatable').DataTable(abp.libs.datatables.normalizeConfiguration({
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
        ajax: abp.libs.datatables.createAjax(service.getListCustomerAllInfo, GetValueSearch),
        columnDefs: [
            {
                title: l('Customer:Name'),
                data: "name",
            },
            {
                title: l('Common:Username'),
                data: "userName",
            },
            {
                title: l('Common:Phone'),
                data: "phone",
            },
            {
                title: l('Common:Email'),
                data: "email",
            },
            {
                title: l('Common:Gender'),
                data: function (data) {
                    return l(data.genderName)
                },
            },
            {
                title: l('Common:DateOfBirth'),
                data: "dateOfBirth",
                render: function (data) {
                    return luxon
                        .DateTime
                        .fromISO(data, {
                            locale: abp.localization.currentCulture.name
                        }).toFormat('dd/MM/yyyy');
                },
            },
            {
                title: l('Common:Address'),
                data: "addressDetail",
            },
            {
                title: l('Actions'),
                rowAction: {
                    items:
                        [
                            {
                                text: l('Edit'),
                                visible: abp.auth.isGranted('CasualManager.CustomerInfo.Update'),
                                action: function (data) {
                                    editModal.open({ id: data.record.id });
                                },
                                displayNameHtml: true,
                            },
                            {
                                text: l('Delete'),
                                visible: abp.auth.isGranted('CasualManager.CustomerInfo.Delete'),
                                confirmMessage: function (data) {
                                    return l('Common:DeletionConfirmationMessage', data.record.name);
                                },
                                action: function (data) {
                                    service.deleteCustomerInfo(data.record.id)
                                        .then(function () {
                                            abp.notify.info(l('Common:SuccessfullyDeleted'));
                                            dataTable.ajax.reload();
                                        });
                                },
                                displayNameHtml: true,
                            }
                        ]
                },
                width: "15%",
                class: "text-center"
            }
        ],
    })
    );

    $('#search').click(function () {
        Search();
    });

    $('#ViewModel_FilterName').keypress(function (event) {
        let keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            Search();
        }
    });

    function Search() {
        dataTable.ajax.reload();
    }

    function GetValueSearch() {
        return {
            'keyword': $("#ViewModel_FilterName").val()
        };
    }

    $('#new-customer').click(function (e) {
        e.preventDefault();
        createModal.open();
    });

    createModal.onResult(function () {
        dataTable.ajax.reload();
        abp.notify.success(l('Common:SuccessfullyCreated'));
    });

    editModal.onResult(function () {
        dataTable.ajax.reload();
        abp.notify.success(l('Common:SuccessfullyEdited'));
    });
});