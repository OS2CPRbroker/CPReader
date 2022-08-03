require.config({
    paths: {
        'jquery': 'jquery-1.11.1',
        'bootstrap': 'bootstrap',
        'jqueryui': 'jquery-ui-1.11.2',
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
            p.processQuery();
        };
        // handle submission routing
        $('#quicksearchbutton').click(function (event) {
            event.preventDefault();
            v.resolveAmbiguousAddresses(callProcessQuery);
        });

        // handle submission routing on enter as well
        $('#query').keypress(function (event) {
            if (event.which === 13) {
                event.preventDefault();
                v.resolveAmbiguousAddresses(callProcessQuery);
            }
        });

        $('#addressQuery').keypress(function (event) {
            if (event.which === 13) {
                event.preventDefault();
                v.resolveAmbiguousAddresses(callProcessQuery);
            }
        });

        // validate name or PNR
        $('#query').keyup(function (event) {
            v.validateQuery();
        });
        $('#query').change(function (event) {
            v.validateQuery();
        });

        // validate address
        $('#addressQuery').keyup(function (event) {
            if (event.which !== 13) {
                v.validateAddressQuery();
            }
        });
        $('#addressQuery').change(function (event) {
            v.validateAddressQuery();
        });

        $('#addressQuery').autocomplete({

            minLength: 1,
            delay: 100,           
            source: function (request, response) {
                $.ajax({
                    url:        "https://api.dataforsyningen.dk/adresser/autocomplete",
                    type:       "GET",
                    dataType:   "jsonp",
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

        $('input[type=radio][name=online]').change(function() {
            v.validateAddressQuery();
        });

        // Disable AJAX Caching to ensure cart actions reach the server in IE 
        $.ajaxSetup({ cache: false });
        setCartButtonEvents();

    }); //end ready
});

