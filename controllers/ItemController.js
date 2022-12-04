/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/

/**
 * Item Save
 * */

$("#btnAddItem").attr('disabled', true);
$("#btnUpdateItem").attr('disabled', true);
$("#btnDeleteItem").attr('disabled', true);
/**
 * Item Save
 * Item ID
 * */
function generateItemID() {
    if (items.length > 0) {
        let lastId = items[items.length - 1].code;
        let digit = lastId.substring(6);
        let number = parseInt(digit) + 1;
        return lastId.replace(digit, number);
    } else {
        return "I00-001";
    }
}

/**
 * Button Add New Item
 * */
$("#btnAddItem").on("click", function () {

    //create object
    let itemsArray = new itemDTO(
        $("#txtItemID").val(),
        $("#txtItemName").val(),
        $("#txtItemQty").val(),
        $("#txtItemPrice").val());

    clearItemTextFields();

    //Alert Save
    saveUpdateAlert("Items", "saved.");

    //Add the customer object to the array
    items.push(itemsArray);

    /* console.log(customers);*/
    $("#txtItemID").val(generateItemID());
    loadAllItems();
    $("#txtItemsCount").text(items.length);
});

/**
 * clear input fields Values Method
 * */
function clearItemTextFields() {
    txtItemID.value = '';
    txtItemName.value = '';
    txtItemQty.value = '';
    txtItemPrice.value = '';
    $("#txtItemName").focus();
    checkValidity(ItemsValidations);
    $("#btnAddItem").attr('disabled', true);
    $("#btnUpdateItem").attr('disabled', true);
    $("#btnDeleteItem").attr('disabled', true);
}

/**
 * load all Item Method
 * */
function loadAllItems() {

    //remove all the table body content before adding data
    $("#ItemTable").empty();

    // get all items records from the array
    for (var item of items) {
        console.log(item);// items object

        // Using String Literals to do the same thing as above
        var row = `<tr><td>${item.code}</td><td>${item.name}</td><td>${item.qty}</td><td>${item.price}</td></tr>`;

        //then add it to the table body of items table
        $("#ItemTable").append(row);
    }
    blindClickEventsItem();
    dblRowClickEventsItem();
    $("#txtItemID").val(generateItemID());
    loadAllItemsForOption();
}

/**
 * Table Listener Click and Load textFields
 * */
function blindClickEventsItem() {
    $("#ItemTable>tr").click(function () {
        let code = $(this).children().eq(0).text();
        let name = $(this).children().eq(1).text();
        let qty = $(this).children().eq(2).text();
        let price = $(this).children().eq(3).text();
        console.log(code, name, qty, price);

        $("#txtItemID").val(code);
        $("#txtItemName").val(name);
        $("#txtItemQty").val(qty);
        $("#txtItemPrice").val(price);

        $("#btnAddItem").attr('disabled', true);
        $("#btnDeleteItem").attr('disabled', false);

    });
}

/**
 * Table Listener double click and Click and Remove textFields
 * */
function dblRowClickEventsItem() {
    $("#ItemTable>tr").on('dblclick', function () {
        let deleteItemID = $(this).children().eq(0).text();
        yesNoAlertIDelete(deleteItemID);
    });
}

/**
 * Search id and Load Table
 * */
$("#ItemIdSearch").on("keypress", function (event) {
    if (event.which === 13) {
        var resultI = items.find(({code}) => code === $("#ItemIdSearch").val());
        console.log(resultI);

        if (resultI != null) {
            $("#ItemTable").empty();
            var row = `<tr><td>${resultI.code}</td><td>${resultI.name}</td><td>${resultI.qty}</td><td>${resultI.price}</td></tr>`;
            $("#ItemTable").append(row);

            $("#txtItemID").val(resultI.code);
            $("#txtItemName").val(resultI.name);
            $("#txtItemQty").val(resultI.qty);
            $("#txtItemPrice").val(resultI.price);

            $("#btnAddItem").attr('disabled', true);
            $("#btnDeleteItem").attr('disabled', false);

        } else {
            emptyMassage();
            clearItemTextFields();
            loadAllItems();
        }
    }
});

/**
 * Item Update
 * */

/**
 * Update Button
 * */
$("#btnUpdateItem").on( "click", function() {
    let ItemId = $("#txtItemID").val();
    let response = updateItem(ItemId);
    if (response) {
        saveUpdateAlert(ItemId, "updated.");
        clearItemTextFields();
        loadAllItems();
    } else {
        unSucsessUpdateAlert(ItemId);
    }
});

/**
 * Update Methods
 * */
function updateItem(itemId) {
    let item = searchItem(itemId);
    if (item != null) {
        item.code = $("#txtItemID").val();
        item.name = $("#txtItemName").val();
        item.qty = $("#txtItemQty").val();
        item.price = $("#txtItemPrice").val();
        return true;
    } else {
        return false;
    }
}

/**
 * Item Delete
 * */

/**
 * Delete Button
 * */
$("#btnDeleteItem").on( "click", function() {
    let deleteIID = $("#txtItemID").val();

    yesNoAlertIDelete(deleteIID);
});

/**
 * Delete Methods
 * */
function deleteItems(itemID) {
    let item = searchItem(itemID);
    if (item != null) {
        let indexNumber1 = items.indexOf(item);
        items.splice(indexNumber1, 1);
        loadAllItems();
        clearItemTextFields();
        return true;
    } else {
        return false;
    }
}

/**
 * Search Methods
 * */
function searchItem(itemID) {
    for (let item of items) {
        if (item.code === itemID) {
            return item;
        }
    }
    return null;
}

/**
 * Auto Forces Input Fields Save
 * */
$("#txtItemID").focus();
const regExItemCode = /^(I00-)[0-9]{3,4}$/;
const regExItemName = /^[A-z ]{3,20}$/;
const regExItemPrice = /^[0-9]{1,10}$/;
const regExItemQtyOnHand = /^[0-9]{1,}[.]?[0-9]{1,2}$/;

let ItemsValidations = [];
ItemsValidations.push({
    reg: regExItemCode,
    field: $('#txtItemID'),
    error: 'Item ID Pattern is Wrong : I00-001'
});
ItemsValidations.push({
    reg: regExItemName,
    field: $('#txtItemName'),
    error: 'Item Name Pattern is Wrong : A-z 3-20'
});
ItemsValidations.push({
    reg: regExItemPrice,
    field: $('#txtItemQty'),
    error: 'Item Qty Pattern is Wrong : 0-9 1-10'
});
ItemsValidations.push({
    reg: regExItemQtyOnHand,
    field: $('#txtItemPrice'),
    error: 'Item Salary Pattern is Wrong : 100 or 100.00'
});

//disable tab key of all four text fields using grouping selector in CSS
$("#txtItemID,#txtItemName,#txtItemQty,#txtItemPrice").on('keydown', function (event) {
    if (event.key === "Tab") {
        event.preventDefault();
    }
});

$("#txtItemID,#txtItemName,#txtItemQty,#txtItemPrice").on('keyup', function (event) {
    checkValidity(ItemsValidations);
});

$("#txtItemID,#txtItemName,#txtItemQty,#txtItemPrice").on('blur', function (event) {
    checkValidity(ItemsValidations);
});

$("#txtItemID").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemCode, $("#txtItemID"))) {
        $("#txtItemName").focus();
    } else {
        focusText($("#txtItemID"));
    }
});

$("#txtItemName").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemName, $("#txtItemName"))) {
        focusText($("#txtItemQty"));
    }
});

$("#txtItemQty").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemPrice, $("#txtItemQty"))) {
        focusText($("#txtItemPrice"));
    }
});

$("#txtItemPrice").on('keydown', function (event) {
    if (event.key === "Enter" && check(regExItemQtyOnHand, $("#txtItemPrice"))) {
        if (event.which === 13) {
            $('#btnAddItem').focus();
        }
    }
});

function setButtonStateItemSave(value) {
    if (value > 0) {
        $("#btnAddItem").attr('disabled', true);
    } else {
        $("#btnAddItem").attr('disabled', false);
    }
}

function setButtonStateItemUpdate(value) {
    if (value > 0) {
        $("#btnUpdateItem").attr('disabled', true);
    } else {
        $("#btnUpdateItem").attr('disabled', false);
    }
}