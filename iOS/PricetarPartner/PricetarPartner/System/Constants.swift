//
//  Constants.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

class Constants {
    
//    static let ServerRootUrl = "https://lfrogs.sakura.ne.jp/pricetarpartner/"
    static let ServerRootUrl = "http://localhost/pricetarpartner/"
    static let ServerApiUrl = Constants.ServerRootUrl + "api/appserver.php"

    static let StringEncoding = String.Encoding.utf8
    static let HttpTimeOutInterval = TimeInterval(10)    
}
