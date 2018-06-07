const express = require('express');
const app = express();
const request = require('request');
const cheerio = require('cheerio');

app.get('/', function (req, res) {
    var userid = req.query.userid;
    var password = req.query.password;
    var token = req.query.token;
    var iccid = req.query.iccid;
    var email = req.query.email;
    var email_confirm = req.query.email_confirm;
    var new_password = req.query.new_password;
    var new_password_confirm = req.query.new_password_confirm;
    var info = req.query.info;
    var doc = req.query.doc;
    var credit = req.query.credit;
    var creditestero = req.query.creditestero;
    var option = req.query.option;
    var services = req.query.services;
    var update = req.query.update;
    var change_options = req.query.change_options;
    var change_services = req.query.change_services;
    var activate = req.query.activate;
    var activation_sim = req.query.activation_sim;

    var data_store = {};
    data_store["iliad"] = {};

    var headers = {
        'cookie': 'ACCOUNT_SESSID=' + token
    };

    if (userid != undefined && password != undefined && token != undefined) {
        var formData = {
            'login-ident': userid,
            'login-pwd': password
        }

        var options = {
            url: 'https://www.iliad.it/account/activation-sim',
            method: 'POST',
            headers: headers,
            formData: formData
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                const $ = cheerio.load(body);
                var results = $('body');
                results.each(function (i, result) {
                    var nav = $(result).find('div.current-user').first().text().split('\n');
                    var check = $(result).find('input').attr('name');
                    var user_name = nav[1].split('  ').join('');
                    var user_id = nav[2].split('  ').join('');
                    var user_numtell = nav[3].split('  ').join('');
                    data_store["iliad"] = {};
                    data_store["iliad"]["version"] = {};
                    data_store["iliad"]["user_name"] = {};
                    data_store["iliad"]["user_id"] = {};
                    data_store["iliad"]["user_numtell"] = {};
                    data_store["iliad"]["sim"] = {};

                    data_store["iliad"]["version"] = "5";
                    data_store["iliad"]["user_name"] = user_name;
                    data_store["iliad"]["user_id"] = user_id;
                    data_store["iliad"]["user_numtell"] = user_numtell;

                    if (check == undefined) {
                        data_store["iliad"]["sim"] = 'true';
                    } else {
                        data_store["iliad"]["sim"] = 'false';
                    }

                    res.send(data_store);
                });
            };
        });
    }
    else if (activation_sim == 'true' && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/activation-sim',
            method: 'POST',
            headers: headers
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {

                try {
                    const $ = cheerio.load(body);
                    var results = $('body');

                    var array = [];
                    var array1 = [];

                    results.each(function (i, result) {

                        data_store["iliad"]["user"] = {};
                        data_store["iliad"]["validation"] = {};
                        data_store["iliad"]["shipping"] = {};
                        data_store["iliad"]["sim"] = {};

                        var array = [];
                        var array2 = [];
                        var array3 = [];
                        $(result)
                            .find('h2.title')
                            .each(function (index, element) {
                                array = array.concat([$(element).text()]);
                            });
                        $(result)
                            .find('div.grid-l')
                            .find('div.step__text')
                            .each(function (index, element) {
                                array2 = array2.concat([$(element).find('h4.step__text__title').text()]);
                            });
                        $(result)
                            .find('div.grid-l')
                            .find('div.step__text')
                            .each(function (index, element) {
                                array3 = array3.concat([$(element).find('a').text()]);
                            });

                        var title = $(result).find('div.form-activation').text().split('   ').join('').split('\n')
                        var orderdate = $(result).find('div.step__text').first().text().split('\n')
                        var tracking = $(result).find('a.red').attr('href')
                        var activation = $(result).find('p.explain').text().split('   ').join('').split('  ').join(' ').split('\n')
                        var check = $(result).find('input').attr('name');
                        var order_shipped = $(result).find('div.step__text').find('p').html()

                        activation = activation[1];
                        title = title[1];

                        var offer = array[0].split('\n')[1].split('   ').join('')
                        var order_date = orderdate[2].split('   ').join('')
                        var date = orderdate[3].split('   ').join('')
                        var tracking_text = array3[2]
                        var validation = array2[0]
                        var preparazione = array2[1]
                        var spedizione = array2[2]


                        data_store["iliad"]["shipping"][0] = spedizione;
                        data_store["iliad"]["shipping"][1] = order_shipped;
                        data_store["iliad"]["shipping"][2] = tracking_text;
                        data_store["iliad"]["shipping"][3] = tracking;
                        data_store["iliad"]["sim"][0] = title;
                        data_store["iliad"]["sim"][1] = activation;
                        if (check == undefined) {
                            data_store["iliad"]["sim"][2] = 'true';
                        } else {
                            data_store["iliad"]["sim"][2] = 'false';
                        }
                        data_store["iliad"]["sim"][3] = offer;
                        data_store["iliad"]["validation"][0] = validation;
                        data_store["iliad"]["validation"][1] = order_date;
                        data_store["iliad"]["validation"][2] = date;

                        res.send(data_store)

                    });
                } catch (Exeption) {
                    res.sendStatus(503);
                }
            }
        });
    } else if (email != undefined && email_confirm != undefined && password != undefined && token != undefined) {
        var formData = {
            email: email,
            'email-confirm': email_confirm,
            password: password
        }
        var options = {
            url: 'https://www.iliad.it/account/mes-informations/email',
            method: 'POST',
            headers: headers,
            formData: formData
        };
        request(options, function (error, response, body) {
            if (!error) {
                data_store['iliad'][0] = 'true';
                res.send(data_store);

            }
        });
    } else if (new_password != undefined && new_password_confirm != undefined && password != undefined && token != undefined) {
        var formData = {
            'password-current': password,
            'password-new': new_password,
            'password-new-confirm': new_password_confirm
        }
        var options = {
            url: 'https://www.iliad.it/account/mes-informations/password',
            method: 'POST',
            headers: headers,
            formData: formData
        };
        request(options, function (error, response, body) {
            if (!error) {
                data_store['iliad'][0] = 'true';
                res.send(data_store);

            }
        });
    } else if (iccid != undefined && token != undefined) {
        var formData = {
            iccid: iccid
        }

        var options = {
            url: 'https://www.iliad.it/account/activation-sim',
            method: 'POST',
            headers: headers,
            formData: formData
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                const $ = cheerio.load(body);
                var results = $('body');
                results.each(function (i, result) {

                    data_store["iliad"]["sim"] = {};

                    var sim = $(result)
                        .find('div.flash-error').text().split('   ').join('').split('\n')
                    sim = sim[1];
                    if (sim != 'L\'état actuel de votre SIM ne requiert aucune activation.' && sim != 'Cette SIM a été résiliée et ne peux plus être utilisée.') {
                        data_store["iliad"]["sim"][0] = sim;
                        data_store["iliad"]["sim"][1] = "false";
                    } else {
                        data_store["iliad"]["sim"][0] = sim;
                        data_store["iliad"]["sim"][1] = "true";
                    }

                    res.send(data_store);



                });
            }
        });
    } else if (info == 'true' && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/mes-informations',
            method: 'POST',
            headers: headers,
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                const $ = cheerio.load(body);
                var results = $('body');
                var array = [];
                try {
                    results.each(function (i, result) {
                        $(result)
                            .find('div.infos__content')
                            .each(function (index, element) {
                                array = array.concat([$(element).find('div.infos__text').text()]);
                            });
                        var puk = $(result).find('span.bulle-info').attr('data-help-content');

                        var address_title = array[0].split('\n')[1].split('  ').join('');
                        var address = array[0].split('\n')[3].split('  ').join('');
                        var cap = array[0].split('\n')[5].split('   ').join('');
                        var pay_title = array[1].split('\n')[1].split('  ').join('');
                        var pay_method = array[1].split('\n')[2].split('   ').join('');
                        var mail_title = array[2].split('\n')[1].split('  ').join('');
                        var mail = array[2].split('\n')[2].split('  ').join('');
                        var password_title = array[3].split('\n')[1].split('  ').join('');
                        var password = array[3].split('\n')[2].split('  ').join('');
                        var puk_title = array[4].split('\n')[3].split('     ').join('');
                        var puk_text = array[4].split('\n')[7].split('   ').join('');

                        data_store["iliad"][0] = {};
                        data_store["iliad"][1] = {};
                        data_store["iliad"][2] = {};
                        data_store["iliad"][3] = {};
                        data_store["iliad"][4] = {};

                        data_store["iliad"][0][0] = address_title;
                        data_store["iliad"][0][1] = address;
                        data_store["iliad"][0][2] = cap;
                        data_store["iliad"][0][3] = "";
                        data_store["iliad"][0][4] = "http://android12.altervista.org/res/ic_adress.png";

                        data_store["iliad"][1][0] = pay_title;
                        data_store["iliad"][1][1] = pay_method;
                        data_store["iliad"][1][2] = "";
                        data_store["iliad"][1][3] = "http://android12.altervista.org/res/ic_credit_card.png";


                        data_store["iliad"][2][0] = mail_title;
                        data_store["iliad"][2][1] = mail;
                        data_store["iliad"][2][2] = "Modifica";
                        data_store["iliad"][2][3] = "http://android12.altervista.org/res/ic_email.png";


                        data_store["iliad"][3][0] = password_title;
                        data_store["iliad"][3][1] = password;
                        data_store["iliad"][3][2] = "Modifica";
                        data_store["iliad"][3][3] = "http://android12.altervista.org/res/ic_puk.png";


                        data_store["iliad"][4][0] = puk_title;
                        if (puk.indexOf("puk") > -1)
                            data_store["iliad"][4][1] = puk;
                        else
                            data_store["iliad"][4][1] = "0123456";
                        data_store["iliad"][4][2] = "";
                        data_store["iliad"][4][3] = "http://android12.altervista.org/res/ic_password.png";

                        //res.send(data_store);
                        get_puk(data_store);
                    });
                } catch (Exeption) {
                    //res.send(503);
                }
            }
        });
    } else if (option == 'true' && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/mes-options',
            method: 'POST',
            headers: headers,
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                const $ = cheerio.load(body);
                var results = $('body');
                var array = [];
                var array2 = [];
                var array3 = [];
                try {
                    results.each(function (i, result) {
                        $(result)
                            .find('div.as__status--active')
                            .each(function (index, element) {
                                array = array.concat([$(element).find('span.as__status__text').text()]);
                            });
                        $(result)
                            .find('div.as__status--active')
                            .each(function (index, element) {
                                array2 = array2.concat([$(element).find('i').attr('class')]);
                            });
                        $(result)
                            .find('div.bold')
                            .each(function (index, element) {
                                array3 = array3.concat([$(element).find('a').text()]);
                            });

                        var title_option = $(result).find('h1').text().split('\n')[1].split('   ').join('');

                        var status_text_4g = array[0];
                        var block_number_status_text = array[1];
                        var title_4g = array3[4].split('\n')[2].split('   ').join('');
                        var block_number_title = array3[5].split('\n')[2].split('   ').join('');

                        if (array2[0] == 'icon i-check') {
                            var status_4g = 'true';
                        } else {
                            var status_4g = 'false';
                        }
                        if (array2[1] == 'icon i-check') {
                            var block_number_status = 'true';
                        } else {
                            var block_number_status = 'false';
                        }


                        data_store["iliad"][0] = {};
                        data_store["iliad"][1] = {};
                        data_store["iliad"][2] = {};

                        data_store["iliad"][0][0] = title_option;

                        data_store["iliad"][1][0] = title_4g;
                        data_store["iliad"][1][1] = status_text_4g;
                        data_store["iliad"][1][2] = status_4g;
                        data_store["iliad"][1][3] = ""

                        data_store["iliad"][2][0] = block_number_title;
                        data_store["iliad"][2][1] = block_number_status_text;
                        data_store["iliad"][2][2] = block_number_status;
                        data_store["iliad"][2][3] = "blocage_premium"



                        res.send(data_store);

                    });
                } catch (Exeption) { }
            }
        });
    } else if (services == 'true' && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/mes-services',
            method: 'POST',
            headers: headers,
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                const $ = cheerio.load(body);
                var results = $('body');
                var array = [];
                var array2 = [];
                var array3 = [];
                try {
                    results.each(function (i, result) {
                        $(result)
                            .find('div.as__status--active')
                            .each(function (index, element) {
                                array = array.concat([$(element).find('span.as__status__text').text()]);
                            });
                        $(result)
                            .find('div.as__status--active')
                            .each(function (index, element) {
                                array2 = array2.concat([$(element).find('i').attr('class')]);
                            });
                        $(result)
                            .find('div.bold')
                            .each(function (index, element) {
                                array3 = array3.concat([$(element).find('a').text()]);
                            });

                        var title_service = $(result).find('h1').text().split('\n')[1].split('   ').join('');

                        var block_hidden_number_status_text = array[0];
                        var voice_mail_status_text = array[1];
                        var call_transfer_status_text = array[2];
                        var user_status_text = array[3];
                        var fast_call_status_text = array[4];
                        var filter_status_text = array[5];

                        var block_hidden_number_status_title = array3[4].split('\n')[2].split('   ').join('');
                        var voice_mail_status_title = array3[5].split('\n')[2].split('   ').join('');
                        var call_transfer_status_title = array3[6].split('\n')[2].split('   ').join('');
                        var user_status_title = array3[7].split('\n')[2].split('   ').join('');
                        var fast_call_status_title = array3[8].split('\n')[2].split('   ').join('');
                        var filter_status_title = array3[9].split('\n')[2].split('   ').join('');

                        //Blocco numeri nascosti 
                        if (array2[0] == 'icon i-check') {
                            var block_hidden_number_status = 'true';
                        } else {
                            var block_hidden_number_status = 'false';
                        }
                        //Inoltro verso segreteria telefonica all'estero
                        if (array2[1] == 'icon i-check') {
                            var voice_mail_status = 'true';
                        } else {
                            var voice_mail_status = 'false';
                        }
                        //Protezione contro il trasferimento di chiamate
                        if (array2[2] == 'icon i-check') {
                            var call_transfer_status = 'true';
                        } else {
                            var call_transfer_status = 'false';
                        }
                        //Utente non disponibile 
                        if (array2[3] == 'icon i-check') {
                            var user_status = 'true';
                        } else {
                            var user_status = 'false';
                        }
                        //Chiamate rapide 
                        if (array2[4] == 'icon i-check') {
                            var fast_call_status = 'true';
                        } else {
                            var fast_call_status = 'false';
                        }
                        //Filtro Chiamate & SMS/MMS 
                        if (array2[5] == 'icon i-check') {
                            var filter_status = 'true';
                        } else {
                            var filter_status = 'false';
                        }

                        data_store["iliad"][0] = {};
                        data_store["iliad"][1] = {};
                        data_store["iliad"][2] = {};
                        data_store["iliad"][3] = {};
                        data_store["iliad"][4] = {};
                        data_store["iliad"][5] = {};
                        data_store["iliad"][6] = {};

                        data_store["iliad"][0][0] = title_service;

                        data_store["iliad"][1][0] = block_hidden_number_status_title;
                        data_store["iliad"][1][1] = block_hidden_number_status_text;
                        data_store["iliad"][1][2] = block_hidden_number_status;
                        data_store["iliad"][1][3] = "block_anonymous";



                        data_store["iliad"][2][0] = voice_mail_status_title;
                        data_store["iliad"][2][1] = voice_mail_status_text;
                        data_store["iliad"][2][2] = voice_mail_status;
                        data_store["iliad"][2][3] = "voicemail_roaming";



                        data_store["iliad"][3][0] = call_transfer_status_title;
                        data_store["iliad"][3][1] = call_transfer_status_text;
                        data_store["iliad"][3][2] = call_transfer_status;
                        data_store["iliad"][3][3] = "block_redirect";


                        data_store["iliad"][4][0] = user_status_title;
                        data_store["iliad"][4][1] = user_status_text;
                        data_store["iliad"][4][2] = user_status;
                        data_store["iliad"][4][3] = "absent_subscriber";


                        data_store["iliad"][5][0] = fast_call_status_title;
                        data_store["iliad"][5][1] = fast_call_status_text;
                        data_store["iliad"][5][2] = fast_call_status;
                        data_store["iliad"][5][3] = "speed_dial";


                        data_store["iliad"][6][0] = filter_status_title;
                        data_store["iliad"][6][1] = filter_status_text;
                        data_store["iliad"][6][2] = filter_status;
                        data_store["iliad"][6][3] = "filter_rules";



                        res.send(data_store);

                    });
                } catch (Exeption) { }
            }
        });
    } else if (doc == 'true' && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/mes-conditions',
            method: 'POST',
            headers: headers,
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                const $ = cheerio.load(body);
                var results = $('body');
                var array = [];
                var array2 = [];
                try {
                    results.each(function (i, result) {
                        $(result)
                            .find('div.conso__content')
                            .each(function (index, element) {
                                array = array.concat([$(element).find('div.conso__text').text()]);
                            });
                        $(result)
                            .find('div.conso__content')
                            .each(function (index, element) {
                                array2 = array2.concat([$(element).find('div.conso__text').find('a').attr('href')]);
                            });
                        var condition_title = array[0].split('\n')[1].split('   ').join('')
                        var condition_text = array[0].split('\n')[2].split('   ').join('')
                        var price_title = array[1].split('\n')[1].split('   ').join('')
                        var price_text = array[1].split('\n')[2].split('   ').join('')
                        var condition_doc = 'https://www.iliad.it' + array2[0];
                        var price_doc = 'https://www.iliad.it' + array2[1];

                        data_store["iliad"][0] = {};
                        data_store["iliad"][1] = {};
                        data_store["iliad"][0][0] = condition_title;
                        data_store["iliad"][0][1] = condition_text;
                        data_store["iliad"][0][2] = condition_doc;
                        data_store["iliad"][1][0] = price_title;
                        data_store["iliad"][1][1] = price_text;
                        data_store["iliad"][1][2] = price_doc;
                        res.send(data_store);
                    });
                } catch (Exeption) { }
            }
        });
    } else if (creditestero == 'true' && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/conso-et-factures',
            method: 'POST',
            headers: headers,
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                const $ = cheerio.load(body);
                var results = $('body');
                var array = [];
                var array2 = [];
                try {
                    results.each(function (i, result) {

                        $(result)
                            .find('div.conso__content')
                            .each(function (index, element) {
                                array = array.concat([$(element).find('div.conso__text').text()]);
                            });
                        $(result)
                            .find('div.conso__icon')
                            .each(function (index, element) {
                                array2 = array2.concat([$(element).find('div.wrapper-align').text()]);
                            });

                        var title = $(result).find('h2').text().split('\n')[1].split('   ').join('');

                        var chiamate_title = array2[0].split('\n')[2].split('   ').join('');
                        var sms_title = array2[1].split('\n')[2].split('   ').join('');
                        var data_title = array2[2].split('\n')[5].split('   ').join('');
                        var mms_tittle = array2[3].split('\n')[2].split('   ').join('');

                        var chiamate = array[4].split('\n')[1].split('   ').join('');
                        var consumi_voce = array[4].split('\n')[2].split('   ').join('');
                        var sms = array[5].split('\n')[0].split('   ').join('');
                        var sms_extra = array[5].split('\n')[2].split('   ').join('');
                        var data = array[6].split('\n')[1].split('   ').join('');
                        var data_consumi = array[6].split('\n')[2].split('   ').join('');
                        var mms = array[7].split('\n')[1].split('   ').join('');
                        var mms_consumi = array[7].split('\n')[2].split('   ').join('');

                        data_store["iliad"][0] = {};
                        data_store["iliad"][1] = {};
                        data_store["iliad"][2] = {};
                        data_store["iliad"][3] = {};
                        data_store["iliad"][4] = {};

                        data_store["iliad"][0][0] = title;

                        data_store["iliad"][1][0] = chiamate;
                        data_store["iliad"][1][1] = consumi_voce;
                        data_store["iliad"][1][2] = chiamate_title;
                        data_store["iliad"][1][3] = "http://android12.altervista.org/res/ic_call.png";

                        data_store["iliad"][2][0] = sms;
                        data_store["iliad"][2][1] = sms_extra;
                        data_store["iliad"][2][2] = sms_title;
                        data_store["iliad"][2][3] = "http://android12.altervista.org/res/ic_email.png";

                        data_store["iliad"][3][0] = data;
                        data_store["iliad"][3][1] = data_consumi;
                        data_store["iliad"][3][2] = data_title;
                        data_store["iliad"][3][3] = "http://android12.altervista.org/res/ic_gb.png";

                        data_store["iliad"][4][0] = mms;
                        data_store["iliad"][4][1] = mms_consumi;
                        data_store["iliad"][4][2] = mms_tittle;
                        data_store["iliad"][4][3] = "http://android12.altervista.org/res/ic_mms.png";


                        res.send(data_store)
                    });
                } catch (Exeption) {
                }
            }
        });
    } else if (credit == 'true' && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/conso-et-factures',
            method: 'POST',
            headers: headers,
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                const $ = cheerio.load(body);
                var results = $('body');
                var array = [];
                var array2 = [];
                try {
                    results.each(function (i, result) {

                        $(result)
                            .find('div.conso__content')
                            .each(function (index, element) {
                                array = array.concat([$(element).find('div.conso__text').text()]);
                            });
                        $(result)
                            .find('div.conso__icon')
                            .each(function (index, element) {
                                array2 = array2.concat([$(element).find('div.wrapper-align').text()]);
                            });

                        var title = $(result).find('h2').text().split('\n')[1].split('   ').join('');

                        var chiamate_title = array2[0].split('\n')[2].split('   ').join('');
                        var sms_title = array2[1].split('\n')[2].split('   ').join('');
                        var data_title = array2[2].split('\n')[5].split('   ').join('');
                        var mms_tittle = array2[3].split('\n')[2].split('   ').join('');

                        var chiamate = array[0].split('\n')[1].split('   ').join('')
                        var consumi_voce = array[0].split('\n')[2].split('   ').join('')
                        var sms = array[1].split('\n')[0].split('   ').join('')
                        var sms_extra = array[1].split('\n')[1].split('   ').join('')
                        var data = array[2].split('\n')[1].split('   ').join('')
                        var data_consumi = array[2].split('\n')[2].split('   ').join('')
                        var mms = array[3].split('\n')[1].split('   ').join('')
                        var mms_consumi = array[3].split('\n')[2].split('   ').join('')


                        data_store["iliad"][0] = {};
                        data_store["iliad"][1] = {};
                        data_store["iliad"][2] = {};
                        data_store["iliad"][3] = {};
                        data_store["iliad"][4] = {};

                        data_store["iliad"][0] = {};
                        data_store["iliad"][0][0] = title;

                        data_store["iliad"][1][0] = chiamate;
                        data_store["iliad"][1][1] = consumi_voce;
                        data_store["iliad"][1][2] = chiamate_title;
                        data_store["iliad"][1][3] = "http://android12.altervista.org/res/ic_call.png";

                        data_store["iliad"][2][0] = sms;
                        data_store["iliad"][2][1] = sms_extra;
                        data_store["iliad"][2][2] = sms_title;
                        data_store["iliad"][2][3] = "http://android12.altervista.org/res/ic_email.png";

                        data_store["iliad"][3][0] = data;
                        data_store["iliad"][3][1] = data_consumi;
                        data_store["iliad"][3][2] = data_title;
                        data_store["iliad"][3][3] = "http://android12.altervista.org/res/ic_gb.png";

                        data_store["iliad"][4][0] = mms;
                        data_store["iliad"][4][1] = mms_consumi;
                        data_store["iliad"][4][2] = mms_tittle;
                        data_store["iliad"][4][3] = "http://android12.altervista.org/res/ic_mms.png";

                        res.send(data_store);
                    });
                } catch (Exeption) {
                }
            }
        });
    } else if (change_options == 'true' && update != undefined && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/mes-options?update=' + update + '&activate=' + activate,
            method: 'GET',
            headers: headers
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                data_store['iliad'][0] = 'true';
                res.send(data_store);

            }
        });
    } else if (change_services == 'true' && update != undefined && token != undefined) {
        var options = {
            url: 'https://www.iliad.it/account/mes-services?update=' + update + '&activate=' + activate,
            method: 'GET',
            headers: headers
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                data_store['iliad'][0] = 'true';

                res.send(data_store);
            }
        });
    }

    function get_puk(data_store) {
        res.send(data_store);
    }
});
const server = app.listen(process.env.PORT, function () { });