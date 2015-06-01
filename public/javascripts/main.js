require.config({
    paths: {
        'jquery': 'jquery-1.11.1/jquery-1.11.1.min',
        'bootstrap': 'bootstrap-3.3.1-dist/dist/js/bootstrap.min',
        'jqueryui': 'jquery-ui-1.11.2.custom/jquery-ui',
        'cart': 'cart'
    },
    shim: {
        'bootstrap': ['jquery'],
        'jqueryui': ['jquery']
    }
});

require(["jquery", "bootstrap", "processQuery", "validate", "cart", "modolus11", "jqueryui"], function ($, b, p, v, c, m, ui) {

    // wait for the document to be ready
    $(function () {
        // give focus to the search field
        $('#query').focus();

        var callProcessQuery = function () {
            var query = $('#query').val(); //get the content of the input field
            var addressQuery = $('#addressQuery').val(); //get the content of the input field
            var online = ($("input[name=online]:checked").val() == "true");
            p.processQuery(query, addressQuery, online);
        };

        // handle submission routing
        $('#quicksearchbutton').click(function (event) {
            event.preventDefault();
            callProcessQuery($);
        });

        // handle submission routing on enter as well
        $('#query').keypress(function (event) {
            if (event.which == 13) {
                event.preventDefault();
                callProcessQuery($);
            }
        });

        $('#addressQuery').keypress(function (event) {
            if (event.which == 13) {
                event.preventDefault();
                callProcessQuery($);
            }
        });

        // validate incomming input
        $('#query').keyup(function (event) {
            var queryfield = $('#query'); // the query input field
            var query = queryfield.val(); // value of the input field
            var querygroup = $('#querygroup'); // form-group wrapping input

            v.validateQuery(queryfield, query, querygroup);
        });

        // validate incomming input
        $('#addressQuery').keyup(function (event) {
            var queryfield = $('#addressQuery'); // the query input field
            var query = queryfield.val(); // value of the input field

            //v.validateAddressQuery(queryfield, query);
        });

        $('#addressQuery').autocomplete({

            minLength: 1,
            delay: 100,

            source: function (request, response) {
                $.ajax({
                    url: document.location.protocol + '//dawa.aws.dk/adresser/autocomplete',
                    type: "GET",
                    dataType: "jsonp",
                    data: {q: request.term, maxantal: 11},
                    success: function (data) {
                        var suggestions = [];
                        $.each(data, function (i, val) {
                            //var text = val.vejnavn.navn + ((val.husnr.length === 0) ? '' : ' ' + val.husnr) + ', ' + val.postnummer.nr + ' ' + val.postnummer.navn;
                            var text = val.tekst;
                            suggestions.push(text);
                        });
                        response(suggestions);
                    },
                    error: function (textStatus) {
                        alert("Der kunne ikke hentes data fra AWS API'et. Serveren returnerede følgende:\n'" + textStatus + "'\n\nAdresseforslag virker derfor ikke pt.");
                    }
                });
            }
        });

    }); //end ready
});

