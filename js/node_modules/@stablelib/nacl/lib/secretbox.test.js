"use strict";
// Copyright (C) 2016 Dmitry Chestnykh
// MIT License. See LICENSE file for details.
Object.defineProperty(exports, "__esModule", { value: true });
const secretbox_1 = require("./secretbox");
const hex_1 = require("@stablelib/hex");
describe("secretBox", () => {
    const key = new Uint8Array(32);
    for (let i = 0; i < key.length; i++) {
        key[i] = 1;
    }
    const nonce = new Uint8Array(24);
    for (let i = 0; i < nonce.length; i++) {
        nonce[i] = 2;
    }
    const data = new Uint8Array(64);
    for (let i = 0; i < data.length; i++) {
        data[i] = 3;
    }
    const good = "8442bc313f4626f1359e3b50122b6ce6fe66ddfe7d39d14e637eb4fd5b45beadab55198" +
        "df6ab5368439792a23c87db70acb6156dc5ef957ac04f6276cf6093b84be77ff0849cc" +
        "33e34b7254d5a8f65ad";
    it("should generate correct secretbox", () => {
        expect((0, hex_1.encode)((0, secretbox_1.secretBox)(key, nonce, data), true)).toBe(good);
    });
    it("should open secretbox", () => {
        const result = (0, secretbox_1.openSecretBox)(key, nonce, (0, hex_1.decode)(good));
        expect(result).not.toBeNull();
        expect(result).toEqual(data);
    });
    it("should not open invalid secretbox", () => {
        const bad = (0, hex_1.decode)(good);
        bad[50] = 0;
        expect((0, secretbox_1.openSecretBox)(key, nonce, bad)).toBeNull();
    });
});
//# sourceMappingURL=secretbox.test.js.map