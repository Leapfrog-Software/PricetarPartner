//
//  RegisterUserRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

class RegisterUserRequester {
        
    class func register(email: String, password: String, completion: @escaping ((Bool, String?) -> ())) {
        
        var params = ["command": "registerUser"]
        params["email"] = email.base64Encode() ?? ""
        params["password"] = password.base64Encode() ?? ""
        
        ApiRequester.post(params: params) { result, data in
            if result, let dic = data as? Dictionary<String, Any> {
                if (dic["result"] as? String) == "0", let userId = dic["userId"] as? String {
                    completion(true, userId)
                    return
                }
            }
            completion(false, nil)
        }
    }
}
