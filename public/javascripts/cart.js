
function addPersonToCart (uuid) {
    $.get(
        '/cart/add/' + uuid + '/',
        function(data){
            showMessage(data);
            refreshCartContents();
        }
    );
}

function removePersonFromCart(uuid){
    $.get(
        '/cart/remove/' + uuid + '/',
        function(data){
            refreshCartContents();
        }
    );
}

function emptyCart() {
    $.get(
        '/cart/empty/',
        function(data){
            refreshCartContents();
        }
    );
}

function refreshCartContents()
{
    // Refresh div element
    $('#cartViewModalBody').load(
        '/cart/view/',
        null,
        function(data){
            setCartButtonEvents();
        }
    )
    // Refresh count
    $('span[name="cartCount"]').load(
        '/cart/count/'
    );
}

function showMessage(msgText, spnId){
    if(!spnId)
        spnId = 'message'
    var msgSpan= $('#' + spnId);
    msgSpan.text(msgText);
}

function getCPRNumbersString()
{
    var table = document.getElementById("cart-table-rows");
    var numbers="";

    for (var r = 0, n = table.rows.length; r < n; r++)
    {
        var val = table.rows[r].cells[1].innerHTML;
        numbers+= val + ',';
    }
    if(numbers.length > 0)
        numbers = numbers.substr(0,numbers.length -1);
    return numbers;
}

function copyCprNumbers(){
    var numbers = getCPRNumbersString();
    window.clipboardData.setData('text', numbers);
    var txt = $('#spnCprNumbersCopied')
    msg = txt.attr('txt');
    showMessage(msg,'cartMessage');
}

function selectCPRNumbers(){
    var numbers = getCPRNumbersString();
    document.getElementById('copy-message').style.display = "block";
    document.getElementById('cart-nums').innerHTML = numbers;
    document.getElementById('cart-nums').style.display = "block";

    if (typeof window.getSelection != "undefined" && typeof document.createRange != "undefined"){
        var range = document.createRange();
        range.selectNodeContents(document.getElementById('cart-nums'));
        var sel = window.getSelection();
        sel.removeAllRanges();
        sel.addRange(range);
    }
    else if (typeof document.selection != "undefined" && typeof document.body.createTextRange != "undefined"){
        var textRange = document.body.createTextRange();
        textRange.moveToElementText(document.getElementById('cart-nums'));
        textRange.select();
    }
}

function showParents(event, uuid)
{
    var parentsAnchor = event.target;
    var parentsDivId = '#parents-row'+uuid;
    var parentsDiv = $(parentsDivId);

    if(parentsDiv[0].style.display === 'block' || parentsDiv[0].style.display == '')
    {
        parentsDiv.slideUp("fast");
    }
    else // Show
    {
        if(parentsAnchor.attributes.loaded.value == 'false')
        {
            parentsDiv.load(
                '/search/updateparents/'+uuid+'/',
                null,
                function (data) {
                    if(data == "none")
                    {
                        window.alert("Not available");
                    }
                    else
                    {
                        setCartButtonEvents();
                        parentsAnchor.attributes.loaded.value  = 'true';
                        parentsDiv.slideDown("fast");
                    }
                }
            );
        }
        else
        {
            parentsDiv.slideDown("fast");
        }
    }
}

function setCartButtonEvents(){

    $('a[name=addToCartAnchor]').click(function(event){
        var uuid = event.target.getAttribute('uuid');
        addPersonToCart(uuid);
        return false;
    });
    $('a[name=removeFromCartAnchor]').click(function(event){
        var uuid = event.target.getAttribute('uuid');
        removePersonFromCart(uuid);
        return false;
    });

    $('a[name=clearCartAnchor]').click(function(event){
        emptyCart();
        return false;
    });
}