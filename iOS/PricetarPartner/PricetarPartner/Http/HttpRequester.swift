//
//  HttpRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

class HttpRequester {
    
    class func get(url: String, completion: @escaping ((Bool, Data?) -> ())) {
        HttpRequester.request(url: url, method: "GET", body: nil, completion: completion)
    }
    
    class func post(url: String, params: [String: String]?, completion: @escaping ((Bool, Data?) -> ())) {
        
        var body: Data? = nil
        
        if let params = params {
            let paramsStr = params.map { $0.key + "=" + $0.value }.joined(separator: "&")
            body = paramsStr.data(using: Constants.StringEncoding)
        }
        HttpRequester.request(url: url, method: "POST", body: body, completion: completion)
    }
    
    class func request(url: String, method:String, body: Data?, additionalHeader: [String: String]? = nil, completion: @escaping ((Bool, Data?) -> ())) {
        
        guard let urlRaw = URL(string: url) else {
            completion(false, nil)
            return
        }
        var request = URLRequest(url: urlRaw, cachePolicy: .reloadIgnoringLocalAndRemoteCacheData, timeoutInterval: Constants.HttpTimeOutInterval)
        request.httpMethod = method
        request.httpBody = body
        
        additionalHeader?.keys.forEach { key in
            if let value = additionalHeader?[key] {
                request.addValue(value, forHTTPHeaderField: key)
            }
        }
        
        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
            DispatchQueue.main.async {
                if error == nil, let data = data {
                    completion(true, data)
                } else {
                    completion(false, nil)
                }
            }
        }
        task.resume()
    }
}
