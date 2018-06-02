const express = require('express');
const app = express();
const request = require('request');
const cheerio = require('cheerio');

app.get('/', function (req, res) {
    var userid = req.query.userid
    var password = req.query.password
    var token = req.query.token

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
                    var data_store = {};
                    data_store["iliad"] = {};
                    data_store["iliad"]["user"] = [];
                    data_store["iliad"]["validation"] = [];
                    data_store["iliad"]["tracking"] = [];

                    results.each(function (i, result) {
                        data_store["iliad"]["user"][0] = {};
                        data_store["iliad"]["validation"][0] = {};
                        data_store["iliad"]["tracking"][0] = {};

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
                        var spedizione = array[2]
                        var attivazione = array[3]

                        try {
                            var tracking = $(result)
                                .find('a.red').attr('href')
                            data_store["iliad"]["tracking"][0][0] = tracking;
                        } catch (Exception) {}

                        data_store["iliad"]["user"][0]["user_name"] = user_name;
                        data_store["iliad"]["user"][0]["user_id"] = user_id;
                        data_store["iliad"]["user"][0]["user_numtell"] = user_numtell;
                        data_store["iliad"]["validation"][0][0] = validation;
                        data_store["iliad"]["validation"][0][1] = order_date;
                        data_store["iliad"]["validation"][0][2] = date;

                        res.json(data_store);
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