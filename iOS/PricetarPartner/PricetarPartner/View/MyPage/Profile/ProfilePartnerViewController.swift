//
//  ProfilePartnerViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class ProfilePartnerViewController: KeyboardRespondableViewController {

    @IBOutlet private weak var nicknameTextField: UITextField!
    @IBOutlet private weak var areaLabel: UILabel!
    @IBOutlet private weak var careerLabel: UILabel!
    @IBOutlet private weak var statusLabel: UILabel!
    @IBOutlet private weak var oldPriceTextField: UITextField!
    @IBOutlet private weak var newPriceTextField: UITextField!
    @IBOutlet private weak var dangerousPriceTextField: UITextField!
    @IBOutlet private weak var dangerousMessageTextView: UITextView!
    @IBOutlet private weak var bigPriceTextField: UITextField!
    @IBOutlet private weak var bigMessageTextView: UITextView!
    @IBOutlet private weak var inspectionPriceTextField: UITextField!
    @IBOutlet private weak var inspectionMessageTextView: UITextView!
    @IBOutlet private weak var messageTextView: UITextView!
    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var scrollViewBottomConstraint: NSLayoutConstraint!
    
    private var selectedArea: String?
    private var selectedCareer: String?
    private var selectedStatus: String?
    private let imagePicker = ImagePickerManager()
    private var selectedImage: UIImage?
    
    @IBAction func didEndOnExitTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapArea(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let areaSelect = self.instantiate(storyboard: "MyPage", identifier: "ProfileAreaSelectViewController") as! ProfileAreaSelectViewController
        areaSelect.set(defaultArea: self.selectedArea, completion: { [weak self] area in
            self?.selectedArea = area
            self?.areaLabel.text = area
        })
        self.parent?.stack(viewController: areaSelect, animationType: .none)
    }
    
    @IBAction func onTapCareer(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let dataArray = ["1年未満", "1 〜 3年", "3 〜 5年", "5 〜 10年", "10年以上"]

        var defaultIndex: Int? = nil
        if let selectedCareer = self.selectedCareer {
            defaultIndex = dataArray.firstIndex(of: selectedCareer)
        }
        if let parent = self.parent {
            PickerViewController.show(on: parent, title: "Amazon販売キャリア・年数", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
                self?.selectedCareer = dataArray[index]
                self?.careerLabel.text = dataArray[index]
            })
        }
    }
    
    @IBAction func onTapStatus(_ sender: Any) {
        
        self.view.endEditing(true)
        
        self.view.endEditing(true)
        
        let dataArray = ["受付中", "休止中"]

        var defaultIndex: Int? = nil
        if let selectedStatus = self.selectedStatus {
            defaultIndex = dataArray.firstIndex(of: selectedStatus)
        }
        if let parent = self.parent {
            PickerViewController.show(on: parent, title: "お仕事の受付状況", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
                self?.selectedStatus = dataArray[index]
                self?.statusLabel.text = dataArray[index]
            })
        }
    }
    
    @IBAction func onTapImage(_ sender: Any) {
        
        self.view.endEditing(true)
        
        if let parent = self.parent {
            self.imagePicker.showPicker(on: parent, type: .photoLibrary, completion: { [weak self] image in
                if let userImage = image.toUserImage() {
                    self?.userImageView.image = userImage
                    self?.selectedImage = userImage
                }
            })
        }
    }
    
    @IBAction func onTapRegister(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let nickname = self.nicknameTextField.text ?? ""
        if nickname.count == 0 {
            Dialog.show(style: .error, title: "エラー", message: "ニックネームの入力がありません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        guard let area = self.selectedArea else {
            Dialog.show(style: .error, title: "エラー", message: "居住エリアが選択されていません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        guard let career = self.selectedCareer else {
            Dialog.show(style: .error, title: "エラー", message: "Amazon販売キャリア・年数が選択されていません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        guard let status = self.selectedStatus else {
            Dialog.show(style: .error, title: "エラー", message: "お仕事の受付状況が選択されていません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        let newPrice = self.newPriceTextField.text ?? ""
        let oldPrice = self.oldPriceTextField.text ?? ""
        if newPrice.count == 0 || oldPrice.count == 0 {
            Dialog.show(style: .error, title: "エラー", message: "基本の作業料金の入力がありません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        let dangerousPrice = self.dangerousPriceTextField.text ?? ""
        let dangerousMessage = self.dangerousMessageTextView.text ?? ""
        let bigPrice = self.bigPriceTextField.text ?? ""
        let bigMessage = self.bigMessageTextView.text ?? ""
        let inspectionPrice = self.inspectionPriceTextField.text ?? ""
        let inspectionMessage = self.inspectionMessageTextView.text ?? ""
        let message = self.messageTextView.text ?? ""
        
        UpdatePartnerProfileRequester.update(nickname: nickname, area: area, career: career, status: status, newPrice: newPrice, oldPrice: oldPrice, dangerousPrice: dangerousPrice, dangerousMessage: dangerousMessage, bigPrice: bigPrice, bigMessage: bigMessage, inspectionPrice: inspectionPrice, inspectionMessage: inspectionMessage, message: message, image: self.selectedImage, completion: { result in
            FetchUserRequester.shared.fetch(completion: { _ in
                Loading.stop()
                
                if result {
                    if let profile = self.parent as? ProfileViewController {
                        profile.didUpdate()
                    }
                } else {
                    Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
                }
            })
        })
    }
    
    override func didChangeKeyboard(option: KeyboardAnimationOption) {
        
        self.scrollViewBottomConstraint.constant = option.height
        
        UIView.animate(withDuration: option.duration, delay: 0, options: option.curve, animations: {
            self.view.layoutIfNeeded()
        })
    }
}
