//
//  PaddingTextField.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class PaddingTextField: UITextField {

    @IBInspectable var padding: CGPoint = CGPoint.zero
    
    override func textRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.insetBy(dx: self.padding.x, dy: self.padding.y)
    }
    
    override func editingRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.insetBy(dx: self.padding.x, dy: self.padding.y)
    }
    
    override func placeholderRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.insetBy(dx: self.padding.x, dy: self.padding.y)
    }
}

