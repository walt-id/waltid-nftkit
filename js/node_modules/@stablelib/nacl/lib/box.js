"use strict";
// Copyright (C) 2016 Dmitry Chestnykh
// MIT License. See LICENSE file for details.
Object.defineProperty(exports, "__esModule", { value: true });
exports.openBox = exports.box = exports.precomputeSharedKey = exports.generateKeyPair = void 0;
const x25519_1 = require("@stablelib/x25519");
const xsalsa20_1 = require("@stablelib/xsalsa20");
const secretbox_1 = require("./secretbox");
const wipe_1 = require("@stablelib/wipe");
var x25519_2 = require("@stablelib/x25519");
Object.defineProperty(exports, "generateKeyPair", { enumerable: true, get: function () { return x25519_2.generateKeyPair; } });
const zeros16 = new Uint8Array(16);
function precomputeSharedKey(theirPublicKey, mySecretKey) {
    // Compute scalar multiplication result.
    const key = (0, x25519_1.scalarMult)(mySecretKey, theirPublicKey);
    // Hash key with HSalsa function.
    (0, xsalsa20_1.hsalsa)(key, zeros16, key);
    return key;
}
exports.precomputeSharedKey = precomputeSharedKey;
function box(theirPublicKey, mySecretKey, nonce, data) {
    const sharedKey = precomputeSharedKey(theirPublicKey, mySecretKey);
    const result = (0, secretbox_1.secretBox)(sharedKey, nonce, data);
    (0, wipe_1.wipe)(sharedKey);
    return result;
}
exports.box = box;
function openBox(theirPublicKey, mySecretKey, nonce, data) {
    const sharedKey = precomputeSharedKey(theirPublicKey, mySecretKey);
    const result = (0, secretbox_1.openSecretBox)(sharedKey, nonce, data);
    (0, wipe_1.wipe)(sharedKey);
    return result;
}
exports.openBox = openBox;
//# sourceMappingURL=box.js.map