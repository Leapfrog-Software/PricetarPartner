//
//  ApiRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

class ApiRequester {
    
    class func post(params: [String: String]?, completion: @escaping ((Bool, Any?) -> ())) {
        
        HttpRequester.post(url: Constants.ServerApiUrl, params: params) { result, data in
            if result, let data = data {
                do {
                    if let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? Dictionary<String, Any> {
                        completion(true, json)
                        return
                    }
                } catch {}
            }
            completion(false, nil)
        }
    }
}
