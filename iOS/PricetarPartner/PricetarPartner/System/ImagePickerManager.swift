//
//  ImagePickerManager.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/22.
//

import UIKit

class ImagePickerManager: NSObject {
    
    private var completion: ((UIImage) -> ())?
    
    func showPicker(on viewController: UIViewController, type: UIImagePickerController.SourceType, completion: @escaping ((UIImage) -> ())) {
        
        if UIImagePickerController.isSourceTypeAvailable(type) {
            let picker = UIImagePickerController()
            picker.sourceType = type
            picker.delegate = self
            viewController.present(picker, animated: true, completion: nil)
            
            self.completion = completion
        }
    }
}

extension ImagePickerManager: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        if let rawImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            self.completion?(rawImage)
        }
        picker.dismiss(animated: true, completion: nil)
    }
}
