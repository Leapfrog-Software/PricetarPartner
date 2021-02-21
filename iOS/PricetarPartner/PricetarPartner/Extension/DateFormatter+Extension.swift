//
//  DateFormatter+Extension.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

extension DateFormatter {
    
    convenience init(dateFormat: String) {
        self.init()
        
        self.locale = Locale(identifier: "ja_JP")
        self.calendar = Calendar(identifier: .gregorian)
        self.dateFormat = dateFormat
    }
}
