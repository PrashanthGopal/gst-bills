import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  standalone: true,
  name: 'findLanguageFromKey',
})
export default class FindLanguageFromKeyPipe implements PipeTransform {
  private languages: { [key: string]: { name: string; rtl?: boolean } } = {
    en: { name: 'English' },
    fr: { name: 'Français' },
    bn: { name: 'বাংলা' },
    hi: { name: 'हिंदी' },
    mr: { name: 'मराठी' },
    pa: { name: 'ਪੰਜਾਬੀ' },
    ta: { name: 'தமிழ்' },
    te: { name: 'తెలుగు' },
    // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
  };

  transform(lang: string): string {
    return this.languages[lang].name;
  }
}
