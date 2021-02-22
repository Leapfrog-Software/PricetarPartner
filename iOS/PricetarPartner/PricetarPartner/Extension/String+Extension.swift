//
//  String+Extension.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

extension String {
    
    func base64Encode() -> String? {
        let data = self.data(using: .utf8)
        return data?.base64EncodedString()
    }
    
    func base64Decode() -> String? {
        let replaced = self.replacingOccurrences(of: " ", with: "+")
        if let data = Data(base64Encoded: replaced) {
            return String(data: data, encoding: .utf8)
        }
        return nil
    }
    
    func substr(start: Int, length: Int) -> String {
        return String(self[self.index(self.startIndex, offsetBy: start)...self.index(self.startIndex, offsetBy: start + length - 1)])
    }
}
