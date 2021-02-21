//
//  CALayer+Extension.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

extension CALayer {
    
    @objc var borderUIColor: UIColor? {
        get {
            return borderColor == nil ? nil : UIColor(cgColor: borderColor!)
        }
        set {
            borderColor = newValue == nil ? nil : newValue!.cgColor
        }
    }
}
