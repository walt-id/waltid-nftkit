"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.TezosRoutes = void 0;
const common_routes_config_1 = require("../common/common.routes.config");
const tezos_controller_1 = __importDefault(require("../tezos/controllers/tezos.controller"));
class TezosRoutes extends common_routes_config_1.CommonRoutesConfig {
    constructor(app) {
        super(app, 'TezosRoutes');
    }
    configureRoutes() {
        this.app
            .route(`/tezos/contract/deployment`)
            .post(tezos_controller_1.default.deployContract);
        return this.app;
    }
}
exports.TezosRoutes = TezosRoutes;
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidGV6b3Mucm91dGVzLmNvbmZpZy5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uL3Rlem9zL3Rlem9zLnJvdXRlcy5jb25maWcudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7O0FBQUEseUVBQWtFO0FBQ2xFLDZGQUFtRTtBQUduRSxNQUFhLFdBQVksU0FBUSx5Q0FBa0I7SUFDL0MsWUFBWSxHQUF3QjtRQUNoQyxLQUFLLENBQUMsR0FBRyxFQUFFLGFBQWEsQ0FBQyxDQUFDO0lBQzlCLENBQUM7SUFFRCxlQUFlO1FBQ1gsSUFBSSxDQUFDLEdBQUc7YUFDUCxLQUFLLENBQUMsNEJBQTRCLENBQUM7YUFDbkMsSUFBSSxDQUNELDBCQUFlLENBQUMsY0FBYyxDQUNqQyxDQUFDO1FBQ0YsT0FBTyxJQUFJLENBQUMsR0FBRyxDQUFDO0lBQ3BCLENBQUM7Q0FDSjtBQWJELGtDQWFDIn0=