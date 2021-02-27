//
//  ProfileClientViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class ProfileClientViewController: KeyboardRespondableViewController {

    @IBOutlet private weak var nicknameTextField: UITextField!
    @IBOutlet private weak var areaLabel: UILabel!
    @IBOutlet private weak var useFreqencyLabel: UILabel!
    @IBOutlet private weak var newConditionLabel: UILabel!
    @IBOutlet private weak var oldConditionLabel: UILabel!
    @IBOutlet private weak var genreLabel: UILabel!
    @IBOutlet private weak var optionLabel: UILabel!
    @IBOutlet private weak var messageTextView: UITextView!
    @IBOutlet private weak var userImageView: UIImageView!
    @IBOutlet private weak var scrollViewBottomConstraint: NSLayoutConstraint!
    
    private var selectedArea: String?
    private var selectedUseFrequency: String?
    private var selectedNewCondition: String?
    private var selectedOldCondition: String?
    private var selectedGenres = [String]()
    private var selectedOptions = [String]()
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
    
    @IBAction func onTapUseFrequency(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let dataArray = ["0 〜 99個", "100 〜 299個", "300 〜 499個", "500 〜 999個", "1000個以上"]

        var defaultIndex: Int? = nil
        if let selectedUseFrequency = self.selectedUseFrequency {
            defaultIndex = dataArray.firstIndex(of: selectedUseFrequency)
        }
        if let parent = self.parent {
            PickerViewController.show(on: parent, title: "月間のご利用見込み数", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
                self?.selectedUseFrequency = dataArray[index]
                self?.useFreqencyLabel.text = dataArray[index]
            })
        }
    }
    
    @IBAction func onTapNewCondition(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let dataArray = ["0%", "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"]

        var defaultIndex: Int? = nil
        if let selectedNewCondition = self.selectedNewCondition {
            defaultIndex = dataArray.firstIndex(of: selectedNewCondition)
        }
        if let parent = self.parent {
            PickerViewController.show(on: parent, title: "お取り扱い商品のコンディション", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
                self?.selectedNewCondition = dataArray[index]
                self?.newConditionLabel.text = dataArray[index]
            })
        }
    }
    
    @IBAction func onTapOldCondition(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let dataArray = ["0%", "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"]

        var defaultIndex: Int? = nil
        if let selectedOldCondition = self.selectedOldCondition {
            defaultIndex = dataArray.firstIndex(of: selectedOldCondition)
        }
        if let parent = self.parent {
            PickerViewController.show(on: parent, title: "お取り扱い商品のコンディション", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
                self?.selectedOldCondition = dataArray[index]
                self?.oldConditionLabel.text = dataArray[index]
            })
        }
    }
    
    @IBAction func onTapGenre(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let genreSelect = self.instantiate(storyboard: "MyPage", identifier: "ProfileGenreSelectViewController") as! ProfileGenreSelectViewController
        genreSelect.set(defaultGenres: self.selectedGenres, completion: { [weak self] genres in
            self?.selectedGenres = genres
            self?.genreLabel.text = genres.joined(separator: ", ")
        })
        self.parent?.stack(viewController: genreSelect, animationType: .horizontal)
    }
    
    @IBAction func onTapOption(_ sender: Any) {

        self.view.endEditing(true)
        
        let dataArray = ["SKU設定希望", "危険物商品あり", "備品支給あり", "商品説明文に追加希望"]
        
        var defaultIndexes = [Int]()
        self.selectedOptions.forEach {
            if let index = dataArray.firstIndex(of: $0) {
                defaultIndexes.append(index)
            }
        }
        
        if let parent = self.parent {
            MultiPickerViewController.show(on: parent, title: "追加オプション", dataArray: dataArray, defaultIndexes: defaultIndexes, completion: { [weak self] indexes in
                let options = indexes.map { dataArray[$0] }
                self?.selectedOptions = options
                self?.optionLabel.text = options.joined(separator: ", ")
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
        
        guard let useFrequency = self.selectedUseFrequency else {
            Dialog.show(style: .error, title: "エラー", message: "月間のご利用見込み数が選択されていません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        guard let newCondition = self.selectedNewCondition, let oldCondition = self.selectedOldCondition else {
            Dialog.show(style: .error, title: "エラー", message: "お取り扱い商品のコンディションが選択されていません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        let message = self.messageTextView.text ?? ""
        
        Loading.start()
        
        UpdateClientProfileRequester.update(nickname: nickname, area: area, useFrequency: useFrequency, newCondition: newCondition, oldCondition: oldCondition, genres: self.selectedGenres, options: self.selectedOptions, message: message, image: self.selectedImage, completion: { result in
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
