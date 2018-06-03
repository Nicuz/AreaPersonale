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

    var data_store = {};
    data_store["iliad"] = {};

    var headers = {
        'authority': 'www.iliad.it',
        'cache-control': 'max-age=0',
        'origin': 'https://www.iliad.it',
        'upgrade-insecure-requests': '1',
        'content-type': 'application/x-www-form-urlencoded',
        'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36',
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'accept-language': 'it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7,pt;q=0.6',
        'cookie': 'ACCOUNT_SESSID=' + token
    };

    if (email != undefined && email_confirm != undefined && password != undefined && token != undefined) {
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
                console.log(data_store);
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
                console.log(data_store);
            }
        });
    } else if (iccid != undefined && token != undefined) {
        var formData = {
            iccid: parseInt(req.query.iccid)
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
                    if (sim != undefined)
                        data_store["iliad"]["sim"][0] = sim;
                    else
                        data_store["iliad"]["sim"][0] = 'Attivazione avvenuta correttamente';

                    res.send(data_store);

                });
            }
        });
    } else if (userid != undefined && password != undefined) {
        var formData = {
            'login-ident': parseInt(userid),
            'login-pwd': '' + password
        }

        var options = {
            url: 'https://www.iliad.it/account/',
            method: 'POST',
            headers: headers,
            formData: formData
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

                        var nav = $(result).find('div.current-user').first().text().split('\n')
                        var user_name = nav[1].split('  ').join('')
                        var user_id = nav[2].split('  ').join('').replace('ID utente: ', '')
                        var user_numtell = nav[3].split('  ').join('').replace('Numero: ', '');

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
                        var order_shipped = $(result).find('div.step__text').find('p').text()

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
                        data_store["iliad"]["user"]["user_name"] = user_name;
                        data_store["iliad"]["user"]["user_id"] = user_id;
                        data_store["iliad"]["user"]["user_numtell"] = user_numtell;
                        data_store["iliad"]["validation"][0] = validation;
                        data_store["iliad"]["validation"][1] = order_date;
                        data_store["iliad"]["validation"][2] = date;
                        res.send(data_store)
                        console.log(data_store);
                    });
                } catch (Exeption) {
                    res.send(503);
                }
            }
        });
    }
    else if (info == 'true' && token != undefined) {
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

                        data_store["iliad"]["address"] = {};
                        data_store["iliad"]["pay"] = {};
                        data_store["iliad"]["mail"] = {};
                        data_store["iliad"]["password"] = {};
                        data_store["iliad"]["puk"] = {};

                        data_store["iliad"]["address"][0] = address_title;
                        data_store["iliad"]["address"][1] = address;
                        data_store["iliad"]["address"][2] = cap;
                        data_store["iliad"]["pay"][0] = pay_title;
                        data_store["iliad"]["pay"][1] = pay_method;
                        data_store["iliad"]["mail"][0] = mail_title;
                        data_store["iliad"]["mail"][1] = mail;
                        data_store["iliad"]["password"][0] = password_title;
                        data_store["iliad"]["password"][1] = password;
                        data_store["iliad"]["puk"][0] = puk_title;
                        data_store["iliad"]["puk"][1] = puk;
                        data_store["iliad"]["puk"][2] = puk_text;

                        res.send(data_store);
                        console.log(data_store);
                    });
                } catch (Exeption) { }
            }
        });
    }
    else if (doc == 'true' && token != undefined) {
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

                        data_store["iliad"]["condition"] = {};
                        data_store["iliad"]["price"] = {};
                        data_store["iliad"]["condition"][0] = condition_title;
                        data_store["iliad"]["condition"][1] = condition_text;
                        data_store["iliad"]["condition"][2] = condition_doc;
                        data_store["iliad"]["price"][0] = price_title;
                        data_store["iliad"]["price"][1] = price_text;
                        data_store["iliad"]["price"][2] = price_doc;
                        res.send(data_store);
                        console.log(data_store);
                    });
                } catch (Exeption) { }
            }
        });
    }
});
exports = module.exports = app;
const server = app.listen(process.env.PORT, function () { });