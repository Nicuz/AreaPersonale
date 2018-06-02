const express = require('express');
const app = express();
const request = require('request');
const cheerio = require('cheerio');

app.get('/', function (req, res) {
    var userid = req.query.userid
    var password = req.query.password
    var token = req.query.token
    var iccid = req.query.iccid

    var data_store = {};
    data_store["iliad"] = {};

    if (iccid != undefined && token != undefined) {
        var headers = {
            'authority': 'www.iliad.it',
            'cache-control': 'max-age=0',
            'origin': 'https://www.iliad.it',
            'upgrade-insecure-requests': '1',
            'content-type': 'application/x-www-form-urlencoded',
            'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36',
            'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'referer': 'https://www.iliad.it/account/activation-sim',
            'accept-language': 'it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7,pt;q=0.6',
            'cookie': 'ACCOUNT_SESSID=' + token
        };

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
        })
    }

    if (userid != undefined && password != undefined) {
        var headers = {
            'authority': 'www.iliad.it',
            'cache-control': 'max-age=0',
            'origin': 'https://www.iliad.it',
            'upgrade-insecure-requests': '1',
            'content-type': 'application/x-www-form-urlencoded',
            'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36',
            'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'referer': 'https://www.iliad.it/account/',
            'accept-language': 'it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7,pt;q=0.6',
            'cookie': 'ACCOUNT_SESSID=' + token
        };

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

                        // list user_name, user_id, user_numtell
                        var nav = $(result)
                            .find('div.current-user').first().text().split('\n')
                        var user_name = nav[1].split('  ').join('')
                        var user_id = nav[2].split('  ').join('').replace('ID utente: ', '')
                        var user_numtell = nav[3].split('  ').join('').replace('Numero: ', '')


                        $(result)
                            .find('div.grid-l')
                            .find('div.step__text')
                            .each(function (index, element) {
                                array = array.concat([$(element).find('h4.step__text__title').text()]);
                            });
                        var validation = array[0]

                        var orderdate = $(result)
                            .find('div.step__text').first().text().split('\n')
                        var order_date = orderdate[2].split('   ').join('')
                        var date = orderdate[3].split('   ').join('')

                        var preparazione = array[1]


                        try {

                            results.each(function (i, result) {
                                data_store["iliad"]["sim"] = {};

                                var activation = $(result)
                                    .find('p.explain').text().split('   ').join('').split('\n')
                                activation = activation[1];

                                var title = $(result)
                                    .find('div.form-activation').text().split('   ').join('').split('\n')
                                title = title[1];

                                data_store["iliad"]["sim"][0] = activation;
                                data_store["iliad"]["sim"][1] = title;

                            });

                        } catch (Exception) {}

                        try {
                            var spedizione = array[2]
                            var tracking = $(result)
                                .find('a.red').attr('href')
                            var order_shipped = $(result)
                                .find('div.step__text').find('p').text()
                            data_store["iliad"]["shipping"][0] = spedizione;
                            data_store["iliad"]["shipping"][1] = order_shipped;
                            data_store["iliad"]["shipping"][2] = tracking;
                        } catch (Exception) {}

                        data_store["iliad"]["user"]["user_name"] = user_name;
                        data_store["iliad"]["user"]["user_id"] = user_id;
                        data_store["iliad"]["user"]["user_numtell"] = user_numtell;
                        data_store["iliad"]["validation"][0] = validation;
                        data_store["iliad"]["validation"][1] = order_date;
                        data_store["iliad"]["validation"][2] = date;

                        res.send(data_store);
                    });
                } catch (Exeption) {
                    res.send(503);
                }
            }
        })
    }
});
exports = module.exports = app;
const server = app.listen(process.env.PORT, function () {});