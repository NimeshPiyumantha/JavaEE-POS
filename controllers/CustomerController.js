/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/

/**
 * Customer Save
 * */

$("#btnSaveCustomer").attr('disabled', true);
$("#btnUpdateCustomer").attr('disabled', true);
$("#btnDeleteCustomer").attr('disabled', true);
/**
 * Customer Save
 * Customer ID
 * */
function generateCustomerID() {
    if (customers.length > 0) {
        let lastId = customers[customers.length - 1].id;
        let digit = lastId.substring(6);
        let number = parseInt(digit) + 1;
        return lastId.replace(digit, number);
    } else {
        return "C00-001";
    }
}

/**
 * Button Add New Customer
 * */
$("#btnSaveCustomer").on( "click", function() {

    //create object
    let CustomerArray = new customerDTO(
        $("#txtCusId").val(),
        $("#txtCusName").val(),
        $("#txtCusAddress").val(),
        $("#txtContactNumber").val());

    clearCusTextFields();

    //Alert Save
    saveUpdateAlert("Customer", "saved.");

    //Add the customer object to the array
    customers.push(CustomerArray);

    /* console.log(customers);*/
    $("#txtCusId").val(generateCustomerID());
    loadAllCustomers();
    $("#txtCustomerCount").text(customers.length);
});

/**
 * clear input fields Values Method
 * */
function clearCusTextFields() {
    txtCusId.value = '';
    txtCusName.value = '';
    txtCusAddress.value = '';
    txtContactNumber.value = '';
    $("#txtCusName").focus();
    checkValidity(customerValidations);
    $("#btnSaveCustomer").attr('disabled', true);
    $("#btnUpdateCustomer").attr('disabled', true);
    $("#btnDeleteCustomer").attr('disabled', true);
}

/**
 * load all customers Method
 * */
function loadAllCustomers() {

    //remove all the table body content before adding data
    $("#customerTable").empty();

    // get all customer records from the array
    for (var customer of customers) {
        console.log(customer);// customer object

        // Using String Literals to do the same thing as above
        var row = `<tr><td>${customer.id}</td><td>${customer.name}</td><td>${customer.address}</td><td>${customer.contact}</td></tr>`;

        //then add it to the table body of customer table
        $("#customerTable").append(row);
    }
    blindClickEvents();
    dblRowClickEventsCus();
    loadAllCustomersForOption();
    $("#txtCusId").val(generateCustomerID());
}

/**
 * Table Listener Click and Load textFields
 * */
function blindClickEvents() {
    $("#customerTable>tr").on( "click", function() {
        let id = $(this).children().eq(0).text();
        let name = $(this).children().eq(1).text();
        let address = $(this).children().eq(2).text();
        let contact = $(this).children().eq(3).text();
        console.log(id, name, address, contact);

        $("#txtCusId").val(id);
        $("#txtCusName").val(name);
        $("#txtCusAddress").val(address);
        $("#txtContactNumber").val(contact);

        $("#btnSaveCustomer").attr('disabled', true);
        $("#btnDeleteCustomer").attr('disabled', false);

    });
}

/**
 * Table Listener double click and Click and Remove textFields
 * */
function dblRowClickEventsCus() {
    $("#customerTable>tr").on('dblclick', function () {
        let deleteCusID = $(this).children().eq(0).text();
        yesNoAlertDelete(deleteCusID);
    });
}

/**
 * Search id and Load Table
 * */
$("#searchCusId").on( "keypress", function(event) {
    if (event.which === 13) {
        var result = customers.find(({id}) => id === $("#searchCusId").val());
        console.log(result);

        if (result != null) {
            $("#customerTable").empty();
            var row = `<tr><td>${result.id}</td><td>${result.name}</td><td>${result.address}</td><td>${result.contact}</td></tr>`;
            $("#customerTable").append(row);

            $("#txtCusId").val(result.id);
            $("#txtCusName").val(result.name);
            $("#txtCusAddress").val(result.address);
            $("#txtContactNumber").val(result.contact);

            $("#btnSaveCustomer").attr('disabled', true);
            $("#btnDeleteCustomer").attr('disabled', false);

        } else {
            emptyMassage();
            clearCusTextFields();
            loadAllCustomers();
        }
    }
});

/**
 * Customer Update
 * */

/**
 * Update Button
 * */
$("#btnUpdateCustomer").on( "click", function() {
    let CustomerId = $("#txtCusId").val();
    let response2 = updateCustomers(CustomerId);
    if (response2) {
        saveUpdateAlert(CustomerId, "updated.");
        clearCusTextFields();
        loadAllCustomers();
    } else {
        unSucsessUpdateAlert(CustomerId);
    }
});

/**
 * Update Methods
 * */
function updateCustomers(CustomerId) {
    let customer = searchCustomer(CustomerId);
    if (customer != null) {
        customer.id = $("#txtCusId").val();
        customer.name = $("#txtCusName").val();
        customer.address = $("#txtCusAddress").val();
        customer.contact = $("#txtContactNumber").val();
        return true;
    } else {
        return false;
    }
}

/**
 * Customer Delete
 * */

/**
 * Delete Button
 * */
$("#btnDeleteCustomer").on( "click", function() {
    let deleteID = $("#txtCusId").val();

    yesNoAlertDelete(deleteID);
});

/**
 * Delete Methods
 * */
function deleteCustomer(customerID) {
    let customer = searchCustomer(customerID);
    if (customer != null) {
        let indexNumber = customers.indexOf(customer);
        customers.splice(indexNumber, 1);
        loadAllCustomers();
        clearCusTextFields();
        return true;
    } else {
        return false;
    }
}

/**
 * Search Methods
 * */
function searchCustomer(cusId) {
    for (let customer of customers) {
        if (customer.id === cusId) {
            return customer;
        }
    }
    return null;
}

/**
 * Auto Forces Input Fields Save
 * */
$("#txtCusId").focus();
const regExCusID = /^(C00-)[0-9]{3,4}$/;
const regExCusName = /^[A-z ]{3,20}$/;
const regExCusAddress = /^[A-z0-9/ ]{4,30}$/;
const regExContact = /^(07(0|1|2|4|5|6|7|8)[0-9]{7})$/;

let customerValidations = [];
customerValidations.push({
    reg: regExCusID,
    field: $('#txtCusId'),
    error: 'Customer ID Pattern is Wrong : C00-001'
});
customerValidations.push({
    reg: regExCusName,
    field: $('#txtCusName'),
    error: 'Customer Name Pattern is Wrong : A-z 3-20'
});
customerValidations.push({
    reg: regExCusAddress,
    field: $('#txtCusAddress'),
    error: 'Customer Address Pattern is Wrong : A-z 0-9 ,/'
});
customerValidations.push({
    reg: regExContact,
    field: $('#txtContactNumber'),
    error: 'Customer Contact Pattern is Wrong : 07[0|1|2|4|5|6|7|8]'
});

//disable tab key of all four text fields using grouping selector in CSS
$("#txtCusId,#txtCusName,#txtCusAddress,#txtContactNumber").on('keydown', function (event) {
    if (event.key === "Tab") {
        event.preventDefault();
    }
});

$("#txtCusId,#txtCusName,#txtCusAddress,#txtContactNumber").on('keyup', function (event) {
    checkValidity(customerValidations);
});

$("#txtCusId,#txtCusName,#txtCusAddress,#txtContactNumber").on('blur', function (event) {
    checkValidity(customerValidations);
});

$("#txtCusId").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExCusID, $("#txtCusId"))) {
        $("#txtCusName").focus();
    } else {
        focusText($("#txtCusId"));
    }
});

$("#txtCusName").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExCusName, $("#txtCusName"))) {
        focusText($("#txtCusAddress"));
    }
});

$("#txtCusAddress").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExCusAddress, $("#txtCusAddress"))) {
        focusText($("#txtContactNumber"));
    }
});

$("#txtContactNumber").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExContact, $("#txtContactNumber"))) {
        if (event.which === 13) {
            $('#btnSaveCustomer').focus();
        }
    }
});

function setButtonStateCustomerSave(value) {
    if (value > 0) {
        $("#btnSaveCustomer").attr('disabled', true);
    } else {
        $("#btnSaveCustomer").attr('disabled', false);
    }
}

function setButtonStateCustomerUpdate(value) {
    if (value > 0) {
        $("#btnUpdateCustomer").attr('disabled', true);
    } else {
        $("#btnUpdateCustomer").attr('disabled', false);
    }
}